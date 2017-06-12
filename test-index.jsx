var React = require('react');
var PagedTableView = require('./test-paged-table');

//Концепция такая: <Workspace> - для отображения всей модели данных приложения,
// а отдельные грани (разные таблицы) проецируются в this.state.
//Поэтому мы не имеем несколько data с полиморфной startGetPage,
//  а в одном data имеем startGetXXX.
var data = require('./test-data');
var tables = [];
tables.push({id: "emp", title: "Employees", startGetPage: data.startGetEmployees,
    columns: ["fullname","department"]});
tables.push({id: "dep", title: "Departments", startGetPage: data.startGetDepartments,
    columns: ["name"]});

var Workspace = React.createClass({
  render: function() {
    return (
<div>

<div className="col-md-12">
<ul className="pagination pull-left">
  {tables.map(table =>
    <li className={`page-item view-mode ${['','active'][+(this.state.table == table)]}`}
        key={table.id}>
        <a className="page-link" href = "#"
           onClick={() => this.setState((state, props) => ({table: table}))}>{table.title}</a></li>
  )}
</ul>
</div>

{tables.map(table =>
<div className={`col-md-12 visible:${table == this.state.table}`} key={table.id}>
<PagedTableView table={table} columns={table.columns}/>
</div>
)}

</div>
    );
  },

  getInitialState: function() {
    return {
      table: tables[0]
    };
  }
});

function run() {
    var ReactDOM = require('react-dom');
    ReactDOM.render(<Workspace/>, document.getElementById('workspace'));
}

//No idea why so complex, saw this on the Internet :)
const loadedStates = ['complete', 'loaded', 'interactive'];
if (loadedStates.includes(document.readyState) && document.body) {
  run();
} else {
  window.addEventListener('DOMContentLoaded', run, false);
}
