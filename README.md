# README #

### About ###

VideoMaker is an application that uses the FFMPEG video library to generate video presentations from photos or other images, by superimposing text on them. It should work on Windows, Linux, and MacOSX.

### Build ###

The project uses the Maven build framework. To build it, run:
    
    maven clean install

To package the whole thing into a folder, run this after clean install:

    maven package appassembler:assemble

After that you should find a folder called **deploy** with the built application inside.

### Execution ###

There are two script files that launch the application, go.sh and go.bat. Use the first on Linux/Mac, and the second on Windows.

### Enjoy! ###