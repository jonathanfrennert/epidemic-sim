# Simple Epidemic Simulator (Epi)
A epidemic simulator made in JavaFX for experimenting on the effect of different spread-prevention policies.

### How-to
#### Simulation Panes
<img src="https://github.com/J0HNN7G/EpiSim/blob/master/howto/SimulationPanes.png" width="400" height="300"> 
The simulation panes show the simulated world. The humans are represented by the circles. The colour of the circles indicates the       human's status which correspond to the colours in the statistics table.

#### Statistics Table 
<img src="https://github.com/J0HNN7G/EpiSim/blob/master/howto/StatisticsTable.png" width="150"> 
The statistics table shows the status counts for the humans in real time. The colour of the counts on each row indicate the corresponding status of any given human in the simulation panes and area in the stacked area chart.

#### Area Chart
<img src="https://github.com/J0HNN7G/EpiSim/blob/master/howto/AreaChart.png" width="300">
The area chart shows how the status count for the simulation population changes over time, where the colour of the area corresponds to the status being counted.

#### Simulation Player
<img src="https://github.com/J0HNN7G/EpiSim/blob/master/howto/SimulationPlayer.png" width="400">
The simulation player is used to run, pause or reset the current simulation.

#### Parameters
<img src="https://github.com/J0HNN7G/EpiSim/blob/master/howto/Parameters.png" width="300" height="400">
The parameters are for specifying the given conditions in a simulator. When you have changed the parameters to desired values, you press the generate button to load the new simulation.

### Development 
Have interesting ideas for developing this project? Here is the class diagram.
- [class diagram (jpg)](https://www.dropbox.com/s/y2o4s7x7vnb244h/Epi%20Class%20Diagram.jpg?dl=1) 

- [class diagram (visual paradigm)](https://www.dropbox.com/s/drcfb5krf6yli4q/Epi%20Class%20Diagram.vpp?dl=1). 

### Download
If you want to utilize the software outside of a development context, we have installers for Windows.  

- [Epi-1.0 for Windows (msi)](https://www.dropbox.com/s/l94zermzjs9mlfs/Epi-1.0.msi?dl=1)

### Acknowledgments
The icon for our application was made by [Ddara](https://www.iconfinder.com/dDara) ([CC-SA 3.0](https://creativecommons.org/licenses/by-sa/3.0/legalcode)).

#### The following media inspired us to undertake this project.
- Stevens, Harry. “These Simulations Show How to Flatten the Coronavirus Growth Curve.” The Washington Post, WP Company, 14 Mar. 2020, www.washingtonpost.com/graphics/2020/world/corona-simulator/. 

- 3Blue1Brown. “Simulating an epidemic.” Youtube, 27 Mar. 2020, https://www.youtube.com/watch?v=gxAaO2rsdIs&t=36s.

#### The following aided us greatly with implementing our solution in JavaFX.  
- [JavaFX Tutorials](https://code.makery.ch/library/javafx-tutorial/) - These tutorials were amazing, [Marco Jakob](https://code.makery.ch/about/) did an outstanding job with them.

- [JavaFX animation experiments](https://gist.github.com/james-d/8327842) - A bouncing ball simulation project done by [James D](https://gist.github.com/james-d). Provides a large backbone for the wall collision response and model class.

- [JavaFX pane clipping](https://news.kynosarges.org/2016/11/03/javafx-pane-clipping/) - This tutorial helped us with overcoming the issue of JavaFX panes not having a straightforward clipping property, thanks [Christopher Nahr](https://news.kynosarges.org/author/cnahr/).

- [Distributing JavaFX desktop](https://walczak.it/blog/distributing-javafx-desktop-applications-without-requiring-jvm-using-jlink-and-jpackage) - This article by Adam was super useful for creating a JavaFX app installer. 

#### The following provided JavaFX UI assets 
- [JFoenix](http://www.jfoenix.com/) - JFoenix controls provided an elegant and professional styling for our UI controls ([Apache 2.0](http://www.jfoenix.com/documentation.html#License)). 

- [BootstrapFX](https://github.com/kordamp/bootstrapfx) - BootstrapFX makes up most of the Epi's CSS styling.
