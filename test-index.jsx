var React = require('react');
var TableView = require('./test-tabular');
var Pagination = require('./test-pagination');
var data = require('./test-data');

var Workspace = React.createClass({
  render: function() {
    return (
<div>

<TableView items={this.state.items}/>

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

  //Minimize island of components which are held by any pending queries after un-mounting - they will hold this thunk instead
  onDataChange: {
    owner: null,

    process: function (result, pageNum) {
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
    }
  },

  queryData: function(pageNum) {
    var _this = this;
    data.startGetPage(pageNum, pageSize, this.state.filters)
        .then(result => _this.onDataChange.process(result, pageNum));
  },

  componentWillMount: function() {
    this.onDataChange.owner = this;
  },

  componentWillUnmount: function() {
    this.onDataChange.owner = null;
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

//todo make part of component state
var pageSize = 3;
