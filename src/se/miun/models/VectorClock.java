package se.miun.models;

import java.util.HashMap;


/**
 * Access key is userId and the other one is how many messages it has sent
 *                                      userid   messagesSent            */      
public class VectorClock extends HashMap<Integer, Integer> { }