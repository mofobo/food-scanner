poeditor Importer
=================

Python3 Script to import the translations from poeditor to the Android and iOS Project.  

Prerequisites
-------------

- Install python3
- Be sure to have pip
- Install the Requirements: `pip install -r requirements.txt`

Usage
-----

```bash
Usage:
  poeditor_importer.py <project> load all (android|ios) --path=<resources>
  poeditor_importer.py <project> load <languagecode> (android|ios) --path=<resources>
  poeditor_importer.py (-h | --help)

Options:
  -h --help           Show this screen.
  --path=<resources>  Path to the root of resources directory in the git repository.
```
Set `<project>` to the name of the project you're working on (eg. `blsmobil` or `lezzgo`). 
`resources` is the path, where resources are stored in the directory.


### Configuration

Usually, this script is checked out as a git submodule in a project (optional).

To add a new project or to change the list of supported languages, edit the config.ini file.


Running in Android Studio
--------------------------

### Configure as "External Tool":
   
- File >> Settings >> Tools >> External Tools
- Add new external Tool with +
- Set *Name* (e.g. Import Strings)
- Set *Program*: Full path to *import-android-strings.cmd* in *vbe-mobile-common/Scripts/poeditor_importer*
- Set *Working Directory*: Full path to *vbe-mobile-common/Scripts/poeditor_importer*

### Execute:

- Tools >> External Tools >> Import Strings (your defined name)
- You can also add a button to your toolbar as described [here](https://stackoverflow.com/questions/25993042/how-to-add-buttons-linked-to-your-external-tool-in-intelij-idea)
