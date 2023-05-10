Template Data Model Project
===========================

This is a template project for creating a new Data Model using
[vodml tools](https://github.com/ivoa/vo-dml).

Steps to use it
---------------
1. decide on your DM name!
2. copy the repository
3. update the repository to use that name
   1. rename files to use that name rather than TemplateDM*
   2. edit the content of some files
      1. [settings.gradle.kts](settings.gradle.kts) first line needs editing
      2. [vo-dml/TemplateDM-v1.vodml-binding.xml](vo-dml/TemplateDM-v1.vodml-binding.xml) (or at least the renamed version) should have appropriate file and model names used.
      3. [doc/TemplateDM.tex](doc/TemplateDM.tex) should have the included description and model diagram names updated.
         