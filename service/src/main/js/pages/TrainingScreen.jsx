'use strict';

import React, { Component } from "react";
import ReactDOM from "react-dom";
import { Grid, Row , Col } from 'react-bootstrap';

import Menu from "../components/Menu.jsx";
import Bottom from "../components/Bottom.jsx";
import GridNoPaging from "../grid-no-paging.jsx";

ReactDOM.render(
    <Grid >
        <Row>
            <Col><Menu/></Col>
        </Row>
        <Row>
            <Col><GridNoPaging/></Col>
        </Row>
        <Row>
            <Col><Bottom/></Col>
        </Row>
    </Grid>
,document.getElementById('root') )
