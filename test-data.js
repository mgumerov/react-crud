var React = require('react');
var axios = require('axios');

var result = {};

result.startGetDepartments =
    (pageIdx, pageSize, filters) => startGetMockPage(pageIdx, pageSize, filters,
        result => result.data.departments);

result.startGetEmployees =
    (pageIdx, pageSize, filters) => startGetMockPage(pageIdx, pageSize, filters,
        result => result.data.employees);

var startGetMockPage =
    //returns a promise which resolves to {total, page} on success and status-text on failure
    function (pageIdx, pageSize, filters, extractor) {
        //we use those in async handler so let's make sure they are immutable
        if (typeof pageIdx != "number") throw "Number expected";
        if (typeof pageSize != "number") throw "Number expected";

        return axios({
          headers: { 'Content-Type': 'application/json' },
          url: 'data.json',
          params: { page: pageIdx } //url params for GET //todo + filters
        })
        .then( result => {
          var data = extractor(result);
          //In fact, returns empty array if requested a page beyond all available items
          return {
            total: data.length,
            page: data.slice(pageSize*(pageIdx-1), pageSize*pageIdx) //todo proper server-side pagination support
          };
        });
    };

module.exports = result;
