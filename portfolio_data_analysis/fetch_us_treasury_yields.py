from datetime import datetime as dt
import os
import xml.etree.ElementTree as ElTree

import requests

USER_AGENT_HEADERS = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1)"
                                    " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"}
DIRNAME = os.path.dirname(os.path.abspath(__file__))
BASE_URL = "http://data.treasury.gov/feed.svc/DailyTreasuryYieldCurveRateData"
XML_SCHEMA_META = "{http://schemas.microsoft.com/ado/2007/08/dataservices/metadata}"
XML_SCHEMA_CONTENT = "{http://schemas.microsoft.com/ado/2007/08/dataservices}"
STORAGE_PATH = "data/government/bonds/usa/daily/"


def fetch_xml_data():
    print("Fetching data for: {}".format(BASE_URL))
    r = requests.get(BASE_URL, headers=USER_AGENT_HEADERS)
    if r.status_code != 200:
        print("HTTP status_code was not 200. Will try again later")
        print(r.text)
        return None

    return r.text


def convert_treasury_yield_xml_data_to_json(xml_data):
    lines = []
    xml_tree = ElTree.fromstring(xml_data)
    for lev_1_child in xml_tree:
        for lev_2_child in lev_1_child:
            for lev_3_child in lev_2_child:
                if lev_3_child.tag == XML_SCHEMA_META + "properties":
                    tmp_date = ""
                    tmp_value = ""
                    for lev_4_child in lev_3_child:
                        if lev_4_child.tag == XML_SCHEMA_CONTENT + "NEW_DATE":
                            tmp_date = lev_4_child.text
                        if lev_4_child.tag == XML_SCHEMA_CONTENT + "BC_10YEAR":
                            tmp_value = lev_4_child.text
                    if tmp_date is not None and tmp_value is not None:
                        tmp_dt = dt.fromisoformat(tmp_date)
                        tmp_line = '{' + '"date":' + '"' + tmp_dt.strftime("%Y-%m-%d") + '",' + '"ten_year":' + '"' \
                                   + tmp_value + '"}'
                        lines.append(tmp_line)
    return "[" + ",".join(reversed(lines)) + "]"


def fetch_convert_and_save_data():
    raw_data = fetch_xml_data()
    if raw_data is None:
        return

    orig_save_path_file = os.path.join(DIRNAME, STORAGE_PATH + "treasury-yields.xml")
    with open(orig_save_path_file, "w") as orig_file:
        orig_file.write(raw_data)

    path_to_json_file = os.path.join(DIRNAME, STORAGE_PATH + "treasury-yields.json")
    with open(path_to_json_file, "w") as json_file:
        json_file.write(convert_treasury_yield_xml_data_to_json(raw_data))


if __name__ == '__main__':
    fetch_convert_and_save_data()
