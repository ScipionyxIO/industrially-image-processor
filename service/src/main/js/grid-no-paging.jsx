'use strict';

import React from 'react';

import Client from "./tools/client";

import { AgGridReact }  from 'ag-grid-react';
import { ButtonGroup,
            Button,
            Label,
            Alert }  from 'react-bootstrap';
import '../../../node_modules/ag-grid/dist/styles/ag-grid.css';
import '../../../node_modules/ag-grid/dist/styles/ag-theme-balham.css';
import '../../../node_modules/ag-grid/dist/styles/ag-theme-balham-dark.css';
import '../../../node_modules/ag-grid/dist/styles/ag-theme-dark.css';

var columnDefs = [
        { headerName: "Name", field: "name", pinned: "left" , suppressSorting:"false", suppressMenu:"false"},
        { headerName: "Description", field: "description" },
        { headerName: "Model Type", width: 90, field: "modelType" },
        { headerName: "Seed", width: 70, field: "seed", type: ["numericColumn"] },
        { headerName: "Epochs", width: 70, field: "epochs", type: ["numericColumn"] },
        { headerName: "Batch Size", width: 90,  field: "batchSize", type: ["numericColumn"] },
        { headerName: "Split Train Test", width: 100, field: "splitTrainTest", type: ["numericColumn"] },
        { headerName: "Use Transformations", width: 100, field: "useTransformations" },
        { headerName: "Shuffle Transformations", width: 100, field: "shuffleTransformations", type: ["numericColumn"] },
        { headerName: "Number Of Flip Transformations", width: 100, field: "numberOfFlipTransformations", type: ["numericColumn"] },
        { headerName: "Number Of Warp Transformations", width: 100, field: "numberOfWarpTransformations", type: ["numericColumn"] },
        { headerName: "Number Of Color Conversion Transformations", width: 100, field: "numberOfColorConversionTransformations", type: ["numericColumn"] },
        { headerName: "Labels", width: 100, field: "labels" },
        { headerName: "Created Date", width: 100, field: "created" },
        { headerName: "Created By",   width: 100, field: "createdBy" },
    ];

var gridOptions = {
        columnDefs: columnDefs,
        rowData: null,
        enableColResize: true,
        onColumnResized: function(params) {
            console.log(params);
        }
    };


//
class GridNoPaging extends React.Component {

    constructor(props) {
        super(props);
        this.state = { trainings: [] };
    }

    componentDidMount() {
        console.log("App: Loading Trainings");
        Client(
            {
                method: 'GET',
                path: '/rest-api/trainings'
            }).then(
                response => {
                                this.setState(
                                    {
                                        trainings: response.entity._embedded.trainings
                                    });
                            });
    }

    onGridReady(params) {
        this.gridApi = params.api;
        this.gridColumnApi = params.columnApi;
    }

    addNew() {
        console.log("Add new");
    }

    edit() {
        console.log("Edit");
    }

    train() {
        console.log("Train");
    }

    handleRemove() {
        document.querySelector("#alert").show = false;
        document.querySelector("#trainingName").show = false;
        if (confirm('Delete Item <b>test</b>?')){
            console.log("Removed");
        }
    }

    render() {
       return   (
                <div>
                    <h2>Trainings</h2>
                    <Alert bsStyle="warning" id="alert">
                        <strong>Holy guacamole!</strong> Removing Training: <span id="trainingName" />
                    </Alert>
                    <ButtonGroup bsSize="small" tyle={{ height: '100px', width: '100%' }}>
                        <Button onClick={this.addNew}>Add New</Button>
                        <Button onClick={this.edit}>Edit</Button>
                        <Button onClick={this.train}>Train</Button>
                        <Button onClick={this.handleRemove}>Remove</Button>
                    </ButtonGroup>
                    <div
                        style={{ height: '500px', width: '100%'}} className="ag-theme-balham-dark" >
                        <AgGridReact
                            columnDefs={columnDefs}
                            rowData={this.state.trainings}
                            rowSelection={this.state.rowSelection}
                            onGridReady={this.onGridReady.bind(this)}
                            enableSorting="true"
                            rowSelection="single"
                            rowDeselection="single"
                            enableColResize="true">
                        </AgGridReact>
                    </div>
                </div>
                );
    }
}


export default GridNoPaging;