var React = require('react');
var Modal = require('react-bootstrap').Modal;
var PagedTableView = require('./test-paged-table');
var ModalForEmployee = require('./test-modal-employee');

//Концепция такая: <Workspace> - для отображения всей модели данных приложения,
// а отдельные грани (разные таблицы) проецируются в this.state.
//Поэтому мы не имеем несколько data с полиморфной startGetPage,
//  а в одном data имеем startGetXXX.
var data = require('./test-data');

var tableViews = {};

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
<PagedTableView table={table} columns={table.columns} onRowClick={row => this.openModal(table, row)}
  ref={ref => tableViews[table.id] = ref}/>
{table.modalTemplate.renderTemplate(table.getModalData(this.state),
    () => this.closeModal(table), (data) => this.saveModal(table, data))}
</div>
)}

</div>
    );
  },

  //In actual app I'd probably re-query the selected item before displaying it
  //in a modal, both to query less data in table (only some of the fields) and
  //to lessen chances of concurrent modification by other user
  openModal: function (table, row) { this.setState(table.makeModalData(row)) },

  closeModal: function (table) { this.setState(table.makeModalData(null)) },

  saveModal: function(table, data) { table.saveModal(data); this.closeModal(table); },

  getInitialState: function() {
    return {
      modalDepartment: null,
      modalEmployee: null,
      table: tables[0]
    };
  }
});

//По идее это такие же компоненты, но для разнообразия буду просто функцией генерировать
var modalForDepartments = {
  renderTemplate: function (data, onClose, onSave) {
    return data ? (
    <Modal show={Boolean(data)} onHide={onClose}>
       <Modal.Header closeButton>
         <Modal.Title>Edit department</Modal.Title>
       </Modal.Header>
       <Modal.Body>
         <form>
           <input type="text" label="Name" placeholder="Name of department" id="name" value={data.name}/>
         </form>
       </Modal.Body>
       <Modal.Footer><button onClick={()=>onSave(data)}>Save</button><button onClick={onClose}>Close</button></Modal.Footer>
     </Modal>
     ) : null
  }
};

var modalForEmployees = {
  renderTemplate: function (data, onClose, onSave) {
    return data ? <ModalForEmployee employee={data} onClose={onClose} onSave={onSave}/> : null
  }
};

var tables = [];
tables.push({id: "emp", title: "Employees", startGetPage: data.startGetEmployees,
    columns: ["fullname","department"],
    modalTemplate: modalForEmployees,
    makeModalData: (data) => ({modalEmployee: data}),
    getModalData: (state) => state.modalEmployee,
    saveModal: (employee) => {
        data.putEmployee(employee);
        //можно было бы заменять строчку в таблице, но проще сделать рефреш =)
        tableViews.emp.refresh();
        //перезачитывать state.employee не нужно, т.к. модалка закрывается
      }
    });
tables.push({id: "dep", title: "Departments", startGetPage: data.startGetDepartments,
    columns: ["name"],
    modalTemplate: modalForDepartments,
    makeModalData: (data) => ({modalDepartment: data}),
    getModalData: (state) => state.modalDepartment,
    saveModal: (data) => alert(JSON.stringify(data))
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
