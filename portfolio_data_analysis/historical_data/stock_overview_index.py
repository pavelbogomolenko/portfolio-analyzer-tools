import glob
import json

INDEX_OUTPUT_FILENAME = "index.json"


def create_update_stock_overview_index_file_from_path(path):
    overview_files = glob.glob(path + "*.json")
    json_lines = []
    for overview_file in overview_files:
        if overview_file.find(INDEX_OUTPUT_FILENAME) > -1:
            continue
        with open(overview_file) as f:
            try:
                overview_content = json.loads(f.read())
                if not overview_content:
                    continue
                line = '{' + '"Symbol":' + '"' + overview_content["Symbol"].lower() + '",' \
                       '"Name":' + '"' + overview_content["Name"] + '",' \
                       '"Sector":' + '"' + overview_content["Sector"] + '",' \
                       '"Industry":' + '"' + overview_content["Industry"] + '",' \
                       '"Country":' + '"' + overview_content["Country"] + '"' \
                       + '}'
                json_lines.append(line)
            except json.decoder.JSONDecodeError:
                continue
    json_str = '[' + ",".join(json_lines) + ']'

    index_output_file = path + INDEX_OUTPUT_FILENAME
    with open(index_output_file, "w") as output_file:
        output_file.write(json_str)
