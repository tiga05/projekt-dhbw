import { Mongo } from 'meteor/mongo';
export const Kafkadata = new Mongo.Collection('kafka_collection');

//Diese Datei stellt die Verbindung zur collection kafka_collection her. Durch en export kann diese bei Bedarf in die ensptrechenden HTML-Elemente importiert werden.