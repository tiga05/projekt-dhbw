import { Mongo } from 'meteor/mongo';
export const Amqpdata = new Mongo.Collection('amqp_collection');

//Diese Datei stellt die Verbindung zur collection amqp_collection her. Durch en export kann diese bei Bedarf in die ensptrechenden HTML-Elemente importiert werden.