#!/usr/bin/env python3
"""poeditor importer

Small helper to import strings from poeditor Android and iOS Projects.
It also speeds up the process of importing translated text into your project.

Note:
- When importing for iOS, it will replace Android Placeholders with '%@'
- To add a new project, edit the config.ini file


Usage:
  poeditor_importer.py <project> load all (android|ios) --path=<resources>
  poeditor_importer.py <project> load <languagecode> (android|ios) --path=<resources>
  poeditor_importer.py <project> load all (android|ios) --path=<resources> only <terms_filter>
  poeditor_importer.py <project> load <languagecode> (android|ios) --path=<resources> only <terms_filter>
  poeditor_importer.py (-h | --help)
  filters are: 'translated', 'untranslated', 'fuzzy', 'not_fuzzy', 'automatic', 'not_automatic', 'proofread', 'not_proofread'

Options:
  -h --help           Show this screen.
  --path=<resources>  Path to the root of resources directory in the git repository.
"""

import xml.etree.cElementTree as ET
import os
import shutil
import configparser
import requests
from docopt import docopt

"""
Supported platforms
"""

TYPES = {
    'APPLE_STRINGS': 'apple_strings',
    'ANDROID_STRINGS': 'android_strings',
}

"""
Project settings
"""
PLATFORM_IOS = 'iOS'
PLATFORM_ANDROID = 'Android'
AVAILABLE_PLATFORMS = [PLATFORM_ANDROID, PLATFORM_IOS]

"""
iOS Settings
"""
REPLACE_ANDROID_PLACEHOLDERS = ['%s', '%d', '%f', '%1$s', '%1$d', '%2$s', '%2$d', '%3$s', '%3$d', '%4$s', '%4$d']


def load_config(project):
    config = configparser.ConfigParser()
    ini_path = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'config.ini')
    config.read_file(open(ini_path))
    proj = config[project]
    d = dict()

    d['project_id'] = proj['project_id']
    d['api_token'] = proj['api_token']
    d['all_languages'] = proj['all_languages'].strip("\n\n").split("\n")
    return d

def load(language, platform, path, conf, filter = 'automatic'):
    if platform not in AVAILABLE_PLATFORMS:
        print("invalid platform")
        return

    API_TOKEN = conf['api_token']
    PROJECT_ID = conf['project_id']

    file_type = TYPES['ANDROID_STRINGS'] if platform == PLATFORM_ANDROID else TYPES['APPLE_STRINGS']

    print('Fetching Translations for ' + language)
    r = requests.post('https://api.poeditor.com/v2/projects/export', data= {"api_token": API_TOKEN, "id": PROJECT_ID, "language": language, "type": file_type, "filters": filter})

    body = r.json()
    response = body['response']
    if response['status'] != 'success':
        print("Error: ", response)
    else:
        url = body['result']['url']
        r = requests.get(url)
        fp = file_path(path, platform, language)

        if file_type == 'json':
            convert_json_to_xml(fp, r.json(), 'dialect_')
        else:
            with open(fp, 'wb') as fd:
                for chunk in r.iter_content(chunk_size=128):
                    fd.write(chunk)

            if platform == PLATFORM_IOS:
                print("Replace Android placeholders with %@ for iOS")
                replace_in_file(fp, REPLACE_ANDROID_PLACEHOLDERS)
                base_language = conf.get('base_language', None)
                if language == base_language:
                    target_path = fp.replace(language + '.lproj', 'BASE.lproj')
                    shutil.copy(fp, target_path)


def replace_in_file(file_path, replacements):
    lines = []
    with open(file_path) as f:
        for l in f:
            for placeholder in replacements:
                l = l.replace(placeholder, '%@')
            lines.append(l)
    with open(file_path, 'w') as f:
        for l in lines:
            f.write(l)

def file_path(root_path, platform, language):
    """
    returns the file path to the file to be written
    """
    if platform == PLATFORM_ANDROID:
        folder = 'values'
        filename = 'strings.xml'
        if language not in ['en']:
            folder += "-" + language

    else:
        folder = language + '.lproj'
        filename = 'Localizable.strings'

    p = os.path.join(root_path, folder)
    os.makedirs(p, exist_ok=True)
    return os.path.join(p, filename)


def convert_json_to_xml(file_path, content, prefix):
    root = ET.Element("resources")

    for t in content:
        translation = ET.SubElement(root, "string")
        translation.attrib= {
            "name": prefix + t['term'],
            "translatable": 'false'}
        translation.text = '"' + str(t['definition']) + '"'

    tree = ET.ElementTree(root)

    with open(file_path, mode='wb') as f:
        tree.write(f, encoding="utf-8", xml_declaration=True)


if __name__ == '__main__':
    arguments = docopt(__doc__, version='Poeditor Importer 1.0')
    platform = PLATFORM_ANDROID if arguments['android'] else PLATFORM_IOS

    if arguments['load']:
        conf = load_config(arguments['<project>'])

        if arguments['all']:
            languages = conf['all_languages']
        else:
            lang = arguments['<languagecode>']
            if lang not in conf['all_languages']:
                print(lang + " not in supported languages")
                exit()
            languages = [lang]

        for language in languages:
            load(language, platform, arguments['--path'], conf, arguments['<terms_filter>'])

        print("Done.")
        exit()

    print(arguments)
