# T4app
## (insert app description here)

### Architecture - MVVM
View: This portion of the architecture is responsible for presenting data to user, all xml files and activities (Activity files within UI FOLDER, XML files within LAYOUT FOLDER)
Model: Responsible for the business logic and data retrieval/updates to and from the local and remote data sources. (DATA FOLDER)
ViewModel: This portion is responsible for connecting the View and Model components. ( UI FOLDER )

##### Benefits of MVVM

##### Alternatives to MVVM

### OOP Principles Used
1) Inheritance ( reduce code duplication)
  1.1) Base Folder within UI folder
    1.1.1) Contains base forms for Acitivity, ViewModel and Fragment files
  1.2) Base Folder within DATA folder
2) Polymorphism
