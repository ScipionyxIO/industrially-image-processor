'use strict';

import React from 'react';

class App extends React.Component {
   render() {
      return (
         <div>
            <Header/>
         </div>
      );
   }
}

class Header extends React.Component {
   render() {
      return (
         <div>
            <h1>Bottom</h1>
         </div>
      );
   }
}

export default App;