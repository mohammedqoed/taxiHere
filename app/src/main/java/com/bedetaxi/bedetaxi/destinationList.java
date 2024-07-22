package com.bedetaxi.bedetaxi;

/**
 * Created by LENOVO on 12/26/2016.
 */

public class destinationList {


        public String Name;
        public int Distance;


        public destinationList(String name, int distance){
            this.Distance=distance;
            this.Name=name;

        }


        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getDistance() {
            return Distance;
        }

        public void setDistance(int distance) {
            Distance = distance;
        }


}
