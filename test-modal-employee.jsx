var React = require('react');
var Modal = require('react-bootstrap').Modal;

var data = require('./test-data');

var ModalForEmployee = React.createClass({

  render: function() {
    return (
    <Modal show={true} onHide={this.props.onClose}>
       <Modal.Header closeButton>
         <Modal.Title>Edit employee</Modal.Title>
       </Modal.Header>
       <Modal.Body>
         <form>
           <input type="text" label="Name" placeholder="Employee name" name="fullname"
           value={this.state.fullname} onChange={this.onValueChange}/>
           <select label="Department" name="depId" value={this.state.depId} onChange={this.onValueChange}>

             <option value={this.props.employee.depId}>{this.props.employee.department}</option>

             {/*(this.state._options || [{id: this.props.employee.depId, name: this.props.employee.department}])
              .map(x => <option value={x.id} key={x.id}>{x.name}</option>)*/}
           </select>
         </form>
       </Modal.Body>
       <Modal.Footer>
         <button onClick={()=>this.callSave(this.state)}>Save</button>
         <button onClick={this.props.onClose}>Close</button>
       </Modal.Footer>
     </Modal>
    );
  },

  onValueChange: function(event) {
    this.setState( { [event.target.name]: event.target.value } );
  },

  getInitialState: function() {
    //todo have this in state.employee instead of state; but that makes
    //setState call more complicated to support creating a new "employee" instead of
    //mutating the current one; as it is now, React takes care of it :)
    return this.props;
  },

  callSave: function(state) {
    var clone = Object.assign({}, this.state);
    delete clone._options;
    this.props.onSave(clone);
  },

  componentDidMount: function() {
    //Тут есть некое противоречие: если отделов может быть так много что есть смысл
    //их запрашивать в таблице постранично, то и в модалке есть смысл отображать отделы
    //постранично; но мне стало лень прикручивать сюда PagedTableView
    data.startGetDepartments(1, 999999, null).then(
      success => this.setState({_options: success.page}));
  }
});

module.exports = ModalForEmployee;
