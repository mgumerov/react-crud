var React = require('react');
var TableView = require('./test-tabular');
var Pagination = require('./test-pagination');

var PagedTableView = React.createClass({

  render: function() {
    return (
    <div>
    <div className="col-md-12">
    <TableView items={this.state.items} columns={this.props.table.columns}/>
    </div>

    <div className="col-md-12 text-center">
    <Pagination thisPage={this.state.page} lastPage={this.state.pageCnt} onClick={this.onPagerClick}/>
    </div>
    </div>
    );
  },

  getInitialState: function() {
    return {
      page: null,
      pageCnt: null,
      items: [],
      filters: {}
    };
  },

  componentDidMount: function() {
    this.queryData(1);
  },

  onPagerClick: function(pageNum) {
    this.queryData(pageNum);
  },

  startGetPage: function(pageIdx, pageSize, filters) {
    return this.props.table.startGetPage(pageIdx, pageSize, filters);
  },

  //Minimize island of components which are held by any pending queries after un-mounting - they will hold this thunk instead
  //To be refedined on creation
  onDataChange: null,

  queryData: function(pageNum) {
    var _this = this;
    this.startGetPage(pageNum, pageSize, this.state.filters)
        .then(result => _this.onDataChange.process(result, pageNum));
  },

  componentWillMount: function() {
    this.onDataChange = new DataChangeHandler(this);
  },

  componentWillUnmount: function() {
    this.onDataChange.unmount();
  }
});

//todo make part of component state
var pageSize = 3;

function DataChangeHandler(owner) {
  this.owner = owner;

  this.process = function (result, pageNum) {
    if (!this.owner)
        return;

    var pgcount = Math.ceil(result.total / pageSize);

    //Если такой страницы уже нет, ничего не делаем и вместо этого потребуем переход на ту, которая по нашим данным есть
    //Может получиться, что фильтр сменился за это время на гуи, и возможно придется еще раз перечитать,
    //  но это уже забота этого следующего порожденного нами асинка, не наша
    if (pageNum > pgcount) {
      //Заметим, что всякий раз номер страницы уменьшается, т.е. вечный цикл невозможен. Но конечно возможно, в теории,
      //адское торможение, если сервер будет постепенно удалять элементы, и успевать каждый раз уменьшить кол-во страниц на 1
      //как раз тогда, когда мы решили перейти на страницу назад; todo возможно, следует после 2-3 попыток сбрасывать сразу на первую страницу.
      //todo Ручной переход на страницу например "1" должен откючать уже запущенные такие "автоматические" коррекционные повторные переходы,
      //  не то они отменят его действие
      this.owner.startSetPage(pgcount, pageSize, this.state.filters);
      return;
    }

    //note the "(" before the map declaration - without it this would be recognized as method body, not the returned value :)
    this.owner.setState((state, props) => ({
        items: result.page,
        page: pageNum,
        pageCnt: pgcount
    }));
  },

  this.unmount = () => owner = null
};

module.exports = PagedTableView;
