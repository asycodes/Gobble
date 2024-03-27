# T4app - food

# Tutorial

- BottomSheet: https://www.youtube.com/watch?v=JL6Ga7aM68M
# T4app
# (insert app description here)

## General Git
1) Create new branch for every new feature from master branch (please create from master branch and pull changes before doing this command)
`git checkout -b feature_name`
2) Always check what files have changed from your session of intense coding
`git status`
3) Add your changes
`git add.`
4) Commit your changes
`git commit -am "feat:added cool feature"`
5) Pull changes from master before pushing to check for updates
`git pull origin master`
6) Push your changes to your branch
`git push origin feature_name`
7) Once your branch is complete, go to github and do a PR to request merge to the master branch

## Other Git commands
1) Switching to other branches
`git switch wtv_branch_name`


## Architecture - MVVM
View: This portion of the architecture is responsible for presenting data to user, all xml files and activities (Activity files within UI FOLDER, XML files within LAYOUT FOLDER)
Model: Responsible for the business logic and data retrieval/updates to and from the local and remote data sources. (DATA FOLDER)
ViewModel: This portion is responsible for connecting the View and Model components. ( UI FOLDER )

### Benefits of MVVM

### Alternatives to MVVM

## OOP Principles Used
1) Inheritance ( reduce code duplication)
  1.1) Base Folder within UI folder
    1.1.1) Contains base forms for Acitivity, ViewModel and Fragment files
  1.2) Base Folder within DATA folder
2) Polymorphism
