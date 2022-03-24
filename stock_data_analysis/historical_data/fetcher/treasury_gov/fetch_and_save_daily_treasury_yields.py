from datetime import datetime as dt
import os
import xml.etree.ElementTree as ElTree

from historical_data.provider.treasury_gov.data import get_daily_treasury_yield_data

XML_SCHEMA_META = "{http://schemas.microsoft.com/ado/2007/08/dataservices/metadata}"
XML_SCHEMA_CONTENT = "{http://schemas.microsoft.com/ado/2007/08/dataservices}"
STORAGE_PATH = "data/government/bonds/usa/daily/"
DIRNAME = os.path.dirname(os.path.abspath(__file__))


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


def fetch_and_save_data():
    raw_data = get_daily_treasury_yield_data()
    if raw_data is None:
        return

    orig_save_path_file = os.path.join(DIRNAME, STORAGE_PATH + "treasury-yields.xml")
    with open(orig_save_path_file, "w") as orig_file:
        orig_file.write(raw_data)

    path_to_json_file = os.path.join(DIRNAME, STORAGE_PATH + "treasury-yields.json")
    with open(path_to_json_file, "w") as json_file:
        json_file.write(convert_treasury_yield_xml_data_to_json(raw_data))


if __name__ == '__main__':
    fetch_and_save_data()
