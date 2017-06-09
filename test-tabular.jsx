var React = require('react');

var TableView = React.createClass({

  columns:
    [
     "fullname",
     "department",
    ]
    .map(column => column.name ? column : { name: column } ),

  render: function() {
    var page = this.props.items;
    return (
<div className="table-responsive">
<table className="table data" id="myTable">
  <thead>
        <tr>
            {this.columns.map((column, i) =>
                <td key={i}> {column.name} </td>
            )}
        </tr>
  </thead>
  <tbody>
    {page.map((row, i) =>
        <tr key={row.id}>
            {this.columns.map((column, i) =>
                <td key={i}>
                  {column.present ? column.present(row[column.name]) : row[column.name]}
                </td>
            )}
        </tr>
    )}
  </tbody>
</table>
</div>
    );
  }
});

module.exports = TableView;
