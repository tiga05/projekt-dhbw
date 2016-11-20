import angular from 'angular';
import angularMeteor from 'angular-meteor';
import uiRouter from 'angular-ui-router';
import template from './view1.html';
import angularCharts from 'angular-chart.js';
import {Kafkadata} from '../../api/kafkadata';
import {Amqpdata} from '../../api/amqpdata';

const name = 'view1';

class View1 {
    constructor($scope, $reactive) {
        'ngInject';
        $reactive(this).attach($scope);
        this.showCharttype = 'ds';
        //cardRow beinhaltet die Grundeinstellungen von der cards, sowie deren Typ.
        this.cardRow = [
            {name: 'Drilling Speed', color: 'white', value: 0, status: 'aktuell', type: 'ds'},
            {name: 'Drilling Heat', color: 'white', value: 0, status: 'aktuell', type: 'dh'},
            {name: 'Milling Speed', color: 'white', value: 0, status: 'aktuell', type: 'ms'},
            {name: 'Milling Heat', color: 'white', value: 0, status: 'aktuell', type: 'mh'}
        ];
        //this.type stellt die verschiedenen Charttypen, die mit angular-charts verwendet werden können, zur Verfügung.
        this.type = ['bar', 'line', 'pie', 'doughnut', 'radar'];
        //In chartRow werden die verschiedenen Diagrammtypen initialisiert. Sie werden später dynamisch mit neuen Daten befüllt
        this.chartRow = [
            {
                name: 'Drilling Speed',
                ctype: 'ds',
                type: 'line',
                labels: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
                series: ['Drilling Speed'],
                data: [[0]],
                datasetOverride: [{yAxisID: 'y-axis-1'}],
                options: {
                    animation: false,
                    scales: {
                        yAxes: [
                            {
                                id: 'y-axis-1',
                                type: 'linear',
                                display: true,
                                position: 'left'

                            }]
                    }
                }
            },
            {
                name: 'Drilling Heat',
                ctype: 'dh',
                type: 'line',
                labels: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
                series: ['Drilling Heat'],
                data: [[0]],
                datasetOverride: [{yAxisID: 'y-axis-1'}],
                options: {
                    animation: false,
                    scales: {
                        yAxes: [
                            {
                                id: 'y-axis-1',
                                type: 'linear',
                                display: true,
                                position: 'left'

                            }]
                    }
                }
            },
            {
                name: 'Milling Speed',
                ctype: 'ms',
                type: 'line',
                labels: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
                series: ['Milling Speed'],
                data: [[0]],
                datasetOverride: [{yAxisID: 'y-axis-1'}],
                options: {
                    animation: false,
                    scales: {
                        yAxes: [
                            {
                                id: 'y-axis-1',
                                type: 'linear',
                                display: true,
                                position: 'left'

                            }]
                    }
                }
            },
            {
                name: 'Milling Heat',
                ctype: 'mh',
                type: 'line',
                labels: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
                series: ['Milling Heat'],
                data: [[0]],
                datasetOverride: [{yAxisID: 'y-axis-1'}],
                options: {
                    animation: false,
                    scales: {
                        yAxes: [
                            {
                                id: 'y-axis-1',
                                type: 'linear',
                                display: true,
                                position: 'left'

                            }]
                    }
                }
            }

        ];

        this.helpers({
            //getCustomerInfos gibt ein Array zurück, in dem die bisher verwendeten Kundennummern, die Anzahl der Aufträge der Kundennummer und die letzte Bestellung gespeichert ist.
            //Diese Abfrage läuft asynchron ab, weshalb es eine Callback-Funktion gibt, die entweder das Ergebnis oder einen Fehler zurückliefert.
            //Das Ergebnis wird in einer Session-Variable gespeichert, die dann außerhalb der Meteor.call Funktion returned wird, sobald ein Ergebnis vorliegt.
            getCustomerInfos(){
                Meteor.call('getCustomerInfos', function (error, result) {
                    if (error) {
                        console.log('Methode getCustomer hat nicht funktioniert');
                    } else {
                        Session.set('customerInfos', result);
                    }
                });
                return Session.get('customerInfos');
            },
            //Mit den folgenden Funktionen werden die Maximma des jeweils angeforderten itemName zurückgegeben. Dabei wird entsprechend durch den jeweils höchsten Umdrehungswert oder Hitzewert bestimmt.
            //Die Funktion _.pluck trimmt das JSON-Objekt auf nur noch den geforderten Wert zurecht.
            getDHeatMaximum(){
                return _.pluck(Kafkadata.find({itemName: 'DRILLING_HEAT'}, {
                    limit: 1,
                    sort: {doubleValue: -1}
                }).fetch(), 'doubleValue');
            },
            getMHeatMaximum(){
                return _.pluck(Kafkadata.find({itemName: 'MILLING_HEAT'}, {
                    limit: 1,
                    sort: {doubleValue: -1}
                }).fetch(), 'doubleValue');
            },
            getDSpeedMaximum(){
                return _.pluck(Kafkadata.find({itemName: 'DRILLING_SPEED'}, {
                    limit: 1,
                    sort: {intValue: -1}
                }).fetch(), 'intValue');
            },
            getMSpeedMaximum(){
                return _.pluck(Kafkadata.find({itemName: 'MILLING_SPEED'}, {
                    limit: 1,
                    sort: {intValue: -1}
                }).fetch(), 'intValue');
            },
            //Mit den folgenden Funktionen werden die aktuellen Werte des jeweils angeforderten itemName zurückgegeben. Dabei ist die Datenbank-ID ausschlaggebend für den aktuellsten Wert.
            //Die Funktion _.pluck trimmt das JSON-Objekt auf nur noch den geforderten Wert zurecht.
            getDHeatAktuell(){
                return _.pluck(Kafkadata.find({itemName: 'DRILLING_HEAT'}, {
                    limit: 1,
                    sort: {_id: -1}
                }).fetch(), 'doubleValue');
            },
            getMHeatAktuell(){
                return _.pluck(Kafkadata.find({itemName: 'MILLING_HEAT'}, {
                    limit: 1,
                    sort: {_id: -1}
                }).fetch(), 'doubleValue');
            },
            getDSpeedAktuell(){
                return _.pluck(Kafkadata.find({itemName: 'DRILLING_SPEED'}, {
                    limit: 1,
                    sort: {_id: -1}
                }).fetch(), 'intValue');
            },
            getMSpeedAktuell(){
                return _.pluck(Kafkadata.find({itemName: 'MILLING_SPEED'}, {
                    limit: 1,
                    sort: {_id: -1}
                }).fetch(), 'intValue');
            },
            //Mit den folgenden Funktionen werden die letzten 20 Werte des jeweils angeforderten itemName zurückgegeben. Dabei ist die Datenbank-ID ausschlaggebend für den aktuellsten Wert.
            //Die Funktion _.pluck trimmt das JSON-Objekt auf nur noch die geforderten Werte zurecht. Am Ende werden die Daten der entsprechenden Chart hinzugefügt.
            getChartDSpeedAktuell(){
                var temp2 = _.pluck(Kafkadata.find({itemName: 'DRILLING_SPEED'}, {
                    limit: 20,
                    sort: {_id: -1}
                }).fetch(), 'intValue');
                this.chartRow[0].data[0] = temp2;
            },
            getChartMSpeedAktuell(){
                var temp2 = _.pluck(Kafkadata.find({itemName: 'MILLING_SPEED'}, {
                    limit: 20,
                    sort: {_id: -1}
                }).fetch(), 'intValue');
                this.chartRow[2].data[0] = temp2;
            },
            getChartDHeatAktuell(){
                var temp2 = _.pluck(Kafkadata.find({itemName: 'DRILLING_HEAT'}, {
                    limit: 20,
                    sort: {_id: -1}
                }).fetch(), 'doubleValue');
                this.chartRow[1].data[0] = temp2;
            },
            getChartMHeatAktuell(){
                var temp2 = _.pluck(Kafkadata.find({itemName: 'MILLING_HEAT'}, {
                    limit: 20,
                    sort: {_id: -1}
                }).fetch(), 'doubleValue');
                this.chartRow[3].data[0] = temp2;
            },
            //holt sich die aktuelle OrderNumber abhängig von der höchsten ID in der Datenbank. Höchste ID = aktuellster Auftrag.
            //Die Funktion _.pluck trimmt das JSON-Objekt auf nur noch die geforderten Werte zurecht. Am Ende werden die Daten der entsprechenden Chart hinzugefügt.
            getCurrentOrder2(){
                var tempvar1 = _.pluck(Kafkadata.find({}, {
                    limit: 1,
                    sort: {_id: -1}
                }, {fields: {orderNumber: 1}}).fetch(), 'orderNumber');
                return tempvar1.toString();
            },
        });
    }
    //Auf der Oberfläche lauscht ein scope auf die Änderung des Status der Card mit einem ng-if. Wenn der Status mit dem in der ng-if Abfrage übereinstimmt, so wird diese Card eingeblendet
    //und alle anderen Cards ausgeblendet.
    changeStatus(statusNeu, type) {
        for (var i = 0; i < this.cardRow.length; i++) {
            if (this.cardRow[i].type == type) {
                this.cardRow[i].status = statusNeu;
                break;
            }
        }
    }
    //Mit einem klick auf den entsprechenden Button in der Oberfläche, wird diese Funktion ausgelöst. Hierüber wird der aktuelle Charttyp gesetzt.
    //Mit einer ng-if Abfrage 'lauscht' der Scope auf der Oberfläche auf die Änderung dieser Variable. Wenn der Diagramm typ mit dem in ng-if abgefragten Typ übereinstimmt,
    //wird dieser eingblendet und alle anderen Diagramme ausgeblendet.
    changeChart(type) {
        this.showCharttype = type;
    }

}

export default angular.module(name, [
    angularMeteor,
    angularCharts,
    uiRouter,
]).component(name, {
    template,
    controllerAs: name,
    controller: View1
})

    .config(config)
    //dies ist ein spezieller Filter, welche alle Werte auf zwei Nachkommastellen rundet.
    .filter('roundup', function () {
        return function (input) {
            if (isNaN(input)) return input;
            return Math.round(input * 10) / 10;
        }
    })

function config($stateProvider) {
    'ngInject';
    $stateProvider
        .state('view1', {
            url: '/Allgemein',
            template: '<view1></view1>'
        });
}
