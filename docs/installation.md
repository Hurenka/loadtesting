# Installation

## Installation for launch only

1. Set jdk-8
2. Download Gatling, extract in some directory, /opt, for instance.
3. Inside this folder there are some neccessary directories:
    - /user-files - contains three folders:
    - ./bodies - bodies for big queries in txt format
    - ./data - feeders for tests
    - ./simulations - directory **load** for declared functions and variables; **scenarios** - for executable files

4. As soon as all files are in their directories, open **/bin** folder and execute:

```bash
sh gatling.sh
```
Results are stored in **/results** folder

5. For debug:
  - Open ./conf/logback.xml
  - Uncomment strings as it recommended in file and launch one more time.

## For developers

1. Install scala и sbt:

```bash
echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update
sudo apt-get install sbt
```

2. Clone /loadtesting, set scala-project
3. In IntellijIDEA set settings/build/buildtools/sbt from bundled to custom with directory:
   `usr/share/sbt/bin/sbt-launch.jar`
4. В IntellijIDEA open terminal, launch sbt, wait for compilation, then type `gatling:testOnly *package.Test*`.

