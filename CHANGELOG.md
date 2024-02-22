# Changelog

All notable changes to this project will be documented in this file.

## [Beta Builds]

## [1.4.5] - 

### Add

- Option to remove duplicate sentences in CSV/TSV exports.
- Multi threading support to increase performance.
- Auto-Tagging option for CSV/TSV exports.

### Fix

- No longer export empty TCF files.


## [1.2.5] - 20.02.2024

### Fix

- Empty fetches no longer appended in JSON view.
- Remove corpora from current fetch if login is required but no valid session token is found.
- TSV not combining multiple corpora.
- JSON fetching problem regarding error messages.
- TSV now correctly saves with ".tsv" extension and tabs.
- Only fetch once if login is required but no valid session toke is found.
- Various UI things.

## [1.2.4] - 16.02.2024

### Fix

- CSVLoader not separating correctly
- Various UI things
- Refactoring

## [1.2.3] - 15.02.2024

### Fix

- Return to home when stopping fetch.
- Exported in wrong format

## [1.2.2] - 15.02.2024

### Add

- Corpora that need login. Use session cookie "dwds_session" value from browser in settings to get access.
- Option to set retry amounts for fetching.

### Fix

- Bug if row was too long

## [1.1.1] - 14.02.2024

### Add

- Stop Button
- Automatic Backups

### Fix

- Bug if no export Folder was selected.
- Refactoring

## [1.0.0] - 14.02.2024

### Add

- Initial Release