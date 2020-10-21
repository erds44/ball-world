## ZY43  https://zy43-hw4-collision-world.herokuapp.com/  (3 Slip Days)  
Design Decisions:  
* Remove pcs in DispachAdapter:  
replace pcs with a map with obejct id as key and object as value to avoid type casting and better incorperate with collision system  
* Add APaintObj:  
both fish and balls implement the interface  
* Add fish strategies:  
some strategies fish and ball can share, some are deisnged specifically for fish  
* Remove UpdateCmd:  
there is no need to hide the implementation of update since dispathadapter can direcly mange the objects  
* Add collision system:  
the collision system is implemented by a event-driven algorithm such that the collision time is pre-calculated  
upon the creation of a object stroed in a priority queue and thus there is no need to check for collision every update  
* Add ICollisionHandler:  
based on different type of objects(fish, ball), handler can decide which resolver to use  
* Add ICollisionResolution:  
resovler gives specific resolution on the collision  
```
Citation:
Author: Robert Sedgewick & Kevin Wayne
Date:   Oct 15 2020
Title:  6.1   Event-Driven Simulation
URL:    https://algs4.cs.princeton.edu/61event/
```

