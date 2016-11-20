import {Meteor} from 'meteor/meteor';
import {Kafkadata} from '../imports/api/kafkadata';
import {Amqpdata} from '../imports/api/amqpdata';
Meteor.methods({
    getCustomerInfos: ()=> {
        var kundendaten = [];
        var kundendatenUngefiltert = Amqpdata.find({}, {fields: {customerNumber: 1, timeStamp: 1}}).fetch();
        var kundennummern = _.unique(_.pluck(kundendatenUngefiltert, "customerNumber"), false);
        var letzteBestellung = [];

        for (var z = 0; z < kundennummern.length; z++) {
            var timestampSize = Amqpdata.find({customerNumber: kundennummern[z]}, {
                limit: 1,
                sort: {timeStamp: -1},
                fields: {timeStamp: 1}
            }).fetch();
            var tempvar = _.pluck(timestampSize, "timeStamp");
            letzteBestellung.push(tempvar[0]);
        }
        var anzahl = [];
        for (var i = 0; i < kundennummern.length; i++) {
            anzahl.push(Amqpdata.find({customerNumber: kundennummern[i]}).count());
            kundendaten.push({kundennummer: kundennummern[i], anzahl: anzahl[i], lastOrder: letzteBestellung[i]});
        }
        return kundendaten;
    }

});