package com.autonomoussystemserver.server.domainModel;

/*
In our database we will have 2 tables (Devices and Distances)
Distances table will hold
id -> a unique auto-generated Id (TYPE: integer)
from -> which is the Id of a device which measures a distance from itself TO the id of a device in column "to" (TYPE: integer)
    distances.from has a reference to a devices.id
to -> the id of a device till which the "from" measures the distance (TYPE: integer)
    distances.to has a reference to a devices.id
distance -> the actual distances in cm (TYPE: integer)
    For example, FROM phone 5 TO phone 4 the DISTANCE is 45 cm
*/

public class Distances {

}
