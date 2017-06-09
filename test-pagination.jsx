var React = require('react');

var Pagination = React.createClass({

  render: function() {
    var page = this.props.thisPage;
    var pageCnt = this.props.lastPage;
    var vis = {
      home: (1 != page),
      prev: (1 != page - 1 && page != 1),
      this: true,
      next: (pageCnt != page + 1 && page != pageCnt),
      end: (pageCnt != page)
    };

    return (
<nav>
  <ul className="pagination tablepages">
    {vis.home ? 
      <li className="page-item"><a className="page-link page-direct" href = "#" onClick={this.onClick}>1</a></li> : null}
    {vis.prev ? 
      <li className="page-item"><a className="page-link page-prev" href = "#"
          onClick={()=>this.onPageClick(this.props.thisPage-1)}>&laquo;</a></li> : null}
    {vis.this ? 
      <li className="page-item active"><a className="page-link page-direct" href = "#" onClick={this.onClick}>{this.props.thisPage}</a></li> : null}
    {vis.next ? 
      <li className="page-item"><a className="page-link page-next" href = "#"
          onClick={()=>this.onPageClick(this.props.thisPage+1)}>&raquo;</a></li> : null}
    {vis.end ? 
      <li className="page-item"><a className="page-link page-direct" href = "#" onClick={this.onClick}>{this.props.lastPage}</a></li> : null}
  </ul>
</nav>
    );
  },

  onPageClick: function (pageNum) {
    if (this.props.onClick)
      this.props.onClick(pageNum);
  },

  onClick: function (event) {
    this.onPageClick(Number(event.target.innerText));
  }
});

module.exports = Pagination;
