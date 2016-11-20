import angular from 'angular';
import angularMeteor from 'angular-meteor';
import uiRouter from 'angular-ui-router';
import template from './view3.html';
import angularCharts from 'angular-chart.js';
import {Kafkadata} from '../../api/kafkadata';
import {Amqpdata} from '../../api/amqpdata';


const name = 'view3';

class View3 {
    constructor($scope, $reactive) {
        'ngInject';
        $reactive(this).attach($scope);
        // Initialisierung der Diagrammvariablen; teilweilse Befüllung mit Dummy-Daten
        this.chartRow = [{name: 'Umdrehungen pro Minute', type: 'line', labels: [1, 2, 3], series: ['Drilling Speed','Milling Speed'], data: [[0],[0]],
            datasetOverride: [{yAxisID: 'y-axis-1'}, { yAxisID: 'y-axis-2' }],
            options: {
                scales: {
                    yAxes: [
                        {
                            id: 'y-axis-1',
                            type: 'linear',
                            display: true,
                            position: 'left'

                        },
                        {
                            id: 'y-axis-2',
                            type: 'linear',
                            display: true,
                            position: 'right'
                        }]
                }
            }
        },{name: 'Hitzeverlauf', type: 'line', labels: [1, 2, 3,4,5,6], series: ['Drilling Heat','Milling Heat'], data: [[0],[0]],
            datasetOverride: [{yAxisID: 'y-axis-1'}, { yAxisID: 'y-axis-2' }],
            options: {
                scales: {
                    yAxes: [
                        {
                            id: 'y-axis-1',
                            type: 'linear',
                            display: true,
                            position: 'left'

                        },
                        {
                            id: 'y-axis-2',
                            type: 'linear',
                            display: true,
                            position: 'right'
                        }]
                }
            }
        }];
        //
        this.helpers({
            //getCustomerNumbers gibt ein Array mit Kundennummern zurück.
            //Die Funktion _.pluck trimmt das JSON-Objekt auf nur noch den geforderten Wert zurecht.
            //Die Funktion _.unique sortiert alle mehrfach vorhanden Kundennummern aus.
            getCustomerNumbers(){
                var tempvar=Amqpdata.find().fetch();
                var tempvar2= _.pluck(tempvar,"customerNumber");
                var tempvar3= _.uniq(tempvar2, false);
                return tempvar3;
            },

            //Abhängig von der gewählten Kundennummer gibt diese Funktion ein Array zurück, welches alle Aufträger zu einer bestimmten Kundennummer enthält.
            //this.getReactively sorgt dafür, dass die Anfrage erneut ausgeführt wird, falls die Kundennummer sich ändert.
            getCustomerOrderDetails(){
                return Amqpdata.find({customerNumber: this.getReactively('choosenCustomerNumber')});
            },
            //getCustomerOrderDetailsChartDsMs fügt die Werte für Drilling-Speed und Milling-Speed dynamisch den Diagrammen hinzu.
            //this.getReactively sorgt dafür, dass die Anfrage erneut ausgeführt wird, falls die gewählte Auftragsnummer sich ändert.
            // Die Diagramme bekommen somit dynamisch neue Daten zugewiesen
            //Die Funktion _.pluck trimmt das JSON-Objekt auf nur noch den geforderten Wert, in dem Fall 'intValue', zurecht.
            getCustomerOrderDetailsChartDsMs(){
                var tempvar1= _.pluck(Kafkadata.find({$and:[
                    {orderNumber: this.getReactively('choosenOrderNumber')},
                    {itemName:'DRILLING_SPEED'}]}).fetch(),'intValue');
                var tempvar2= _.pluck(Kafkadata.find({$and:[
                    {orderNumber: this.getReactively('choosenOrderNumber')},
                    {itemName:'MILLING_SPEED'}]}).fetch(),'intValue');
                this.chartRow[0].data[0]=tempvar1;
                this.chartRow[0].data[1]=tempvar2;
            },
            //getCustomerOrderDetailsChartDhMh fügt die Werte für Drilling-Heat und Milling-Heat dynamisch den Diagrammen hinzu.
            //this.getReactively sorgt dafür, dass die Anfrage erneut ausgeführt wird, falls die gewählte Auftragsnummer sich ändert.
            // Die Diagramme bekommen somit dynamisch neue Daten zugewiesen
            //Die Funktion _.pluck trimmt das JSON-Objekt auf nur noch den geforderten Wert, in dem Fall 'doubleValue', zurecht.
            //Dem Diagramm für Drilling Heat, wird in dem Fall aus optischen Gründen noch ein 0-Wert hinzugefügt, da Milling-Heat einen Wert mehr als Drilling-Heat besitzt.
            getCustomerOrderDetailsChartDhMh(){
                var tempvar1= _.pluck(Kafkadata.find({$and:[
                    {orderNumber: this.getReactively('choosenOrderNumber')},
                    {itemName:'DRILLING_HEAT'}]}).fetch(),'doubleValue');
                var tempvar2= _.pluck(Kafkadata.find({$and:[
                    {orderNumber: this.getReactively('choosenOrderNumber')},
                    {itemName:'MILLING_HEAT'}]}).fetch(),'doubleValue');
                tempvar1.push(0);
                this.chartRow[1].data[0]= tempvar1;
                this.chartRow[1].data[1]=tempvar2;
            },
        });

    }
    //Durch einen Klick in der Oberfläche auf die Auftragsnummer, wird diese Funktion ausgelöst
    //Sie weist der Variable 'this.choosenOrderNumber' einen Wert zu, wonach die helpers-Funktionen nach dieser Ordernumber die Hitze-u. Geschwindigkeitsdaten aus der Datenbank heraussuchen.
    //this.checked sorgt, dafür, dass die Diagramm erst angezeigt werden, nachdem eine OrderNumber ausgesucht wurde.
    changeChoosenOrderNumber(input){
        this.choosenOrderNumber=input;
        this.checked=true;
    }
}

export default angular.module(name, [
    angularMeteor,
    angularCharts,
    uiRouter
]).component(name, {
    template,
    controllerAs: name,
    controller: View3
})

    .config(config);

function config($stateProvider) {
    'ngInject';
    $stateProvider
        .state('view3', {
            url: '/Kunden',
            template: '<view3></view3>'
        });
}