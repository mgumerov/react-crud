var React = require('react');
var axios = require('axios');

var result = {};

result.startGetDepartments =
    (pageIdx, pageSize) => startGetPage(pageIdx, pageSize, "departments");

result.startGetEmployees =
    (pageIdx, pageSize) => startGetPage(pageIdx, pageSize, "employees");

result.putEmployee =
    //returns a promise which resolves to xhr result on success and status-text on failure
    function (employee) {
        //we use those in async handler so let's make sure they are immutable
        return axios({
          headers: { 'Content-Type': 'application/json' },
          url: 'http://localhost:8081/' + 'employees/' + encodeURIComponent(employee.id),
          method: 'PUT',
          data: employee
        });
    };

var startGetPage =
    //returns a promise which resolves to {total, page} on success and status-text on failure
    function (pageIdx, pageSize, path) {
        //we use those in async handler so let's make sure they are immutable
        return axios({
          headers: { 'Content-Type': 'application/json' },
          url: 'http://localhost:8081/' + path,
          params: { page: pageIdx, size: pageSize }
        }).then(result => result.data);
    };

module.exports = result;
