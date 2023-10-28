import React from "react";
import PropTypes from "prop-types";
import {
    ButtonGroup,
    Button,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TablePagination,
    TableRow,
    TextField,
} from "@material-ui/core";
import { Link } from "react-router-dom";
import { withRouter } from "react-router";
import { TICKETS_TABLE_COLUMNS } from "../constants/tablesColumns";
import axios from 'axios';

class TicketsTable extends React.Component {
    constructor(props) {
        super(props);

        // this.state = {
        //      page: 1,
        //      rowsPerPage: 10,
        // };
    }

    // handleChangePage = () => {
    //     console.log("change page");
    // };

    handleCancelSubmit = () => {
        console.log("Cancel submit");
    };

    handleSubmitTicket = () => {
        console.log("Submit ticket");
    };

    // handlePageChangeToPrev = () => {
    //     if (this.state.page > 1) {
    //         this.setState({ page: this.state.page - 1 })
    //     }
    // };

    // handlePageChangeToNext = () => {
    //     this.setState({ page: this.state.page + 1 })
    // };

    handleSorting = (value) => {
        console.log("sorting " + value);
    }

    handleActionSubmission = (action, ticketId) => {
        const self = this;
        const token = localStorage.getItem('token');
        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/action"
        const config = {
            headers: {
                'Authorization': token
            },
            params: {
                'action': action
            }
        }

        axios.put(url, null, config).then(response => {
            this.setState({
                actions: response.data
            });
            window.location.reload(true);
        })
    }

    render() {
        const { sortingCallback, searchCallback, tickets } = this.props;
        // const { page, rowsPerPage } = this.state;
        const { url } = this.props.match;
        const {
            handleCancelSubmit,
            handleSubmitTicket,
            // handlePageChangeToPrev,
            // handlePageChangeToNext
            handleSorting
        } = this;

        return (
            <Paper>
                <TableContainer>
                    <TextField
                        onChange={searchCallback}
                        id="filled-full-width"
                        label="Search"
                        style={{ margin: 5, width: "500px" }}
                        placeholder="Search for ticket"
                        margin="normal"
                        InputLabelProps={{
                            shrink: true,
                        }}
                    />
                    <Table>
                        <TableHead>
                            <TableRow>
                                {TICKETS_TABLE_COLUMNS.map((column) => (
                                    <TableCell align={column.align} key={column.id} >
                                        <b class="noselect"
                                            onClick={() => { sortingCallback(column.id) }}
                                        >
                                            {column.label}
                                        </b>
                                    </TableCell>
                                ))}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {tickets
                                // .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                .map((row, index) => {
                                    return (
                                        <TableRow hover role="checkbox" key={index}>
                                            {TICKETS_TABLE_COLUMNS.map((column) => {
                                                const value = row[column.id];
                                                if (column.id === "name") {
                                                    return (
                                                        <TableCell key={column.id}>
                                                            <Link to={`${url}/${row.id}`}>{value}</Link>
                                                        </TableCell>
                                                    );
                                                }
                                                if (column.id === "actions") {
                                                    return value.length != 0 ? (
                                                        <TableCell align="center" key={column.id}>
                                                            {value.map(item => {
                                                                return (<Button variant="outlined" onClick={() => this.handleActionSubmission(item, `${row.id}`)}>
                                                                    {item}
                                                                </Button>);
                                                            })}
                                                            {/* <Button onClick={() => this.handleActionSubmission(value)}>
                                                                {value}
                                                            </Button> */}
                                                        </TableCell>
                                                    ) : (
                                                            <TableCell align="center" key={column.id}>
                                                                <span class="not-assigned-stats">NONE</span>
                                                            </TableCell>
                                                        )
                                                    // return row.status === "draft" ? (
                                                    //     <TableCell align="center" key={column.id}>
                                                    //         <ButtonGroup>
                                                    //             <Button
                                                    //                 onClick={handleCancelSubmit}
                                                    //                 variant="contained"
                                                    //                 color="secondary"
                                                    //             >
                                                    //                 Cancel
                                                    //             </Button>
                                                    //             <Button
                                                    //                 onClick={handleSubmitTicket}
                                                    //                 variant="contained"
                                                    //                 color="primary"
                                                    //             >
                                                    //                 Submit
                                                    //             </Button>
                                                    //         </ButtonGroup>
                                                    //     </TableCell>
                                                    // ) : (
                                                    //         <TableCell key={column.id}></TableCell>
                                                    //     );
                                                } else {
                                                    return <TableCell key={column.id}>{value}</TableCell>;
                                                }
                                            })}
                                        </TableRow>
                                    );
                                })}
                        </TableBody>
                    </Table>
                </TableContainer>
                {/* <TablePagination
                    rowsPerPageOptions={[10]}
                    component="div"
                    count={tickets.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onChangePage={handleChangePage}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                /> */}
            </Paper >
        );
    }
}

TicketsTable.propTypes = {
    searchCallback: PropTypes.func,
    tickets: PropTypes.array,
};

const TicketsTableWithRouter = withRouter(TicketsTable);
export default TicketsTableWithRouter;
