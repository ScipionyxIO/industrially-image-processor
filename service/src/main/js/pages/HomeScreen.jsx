'use strict';

import React, { Component } from "react";
import ReactDOM from "react-dom";
import { Grid, Row , Col } from 'react-bootstrap';

render() {
    const { navigate } = this.props.navigation;
    return {
        <View>
            <Button
                onPress={() => navigate('Chat')}
                title="Chat"
            />
        </View>
    }
}