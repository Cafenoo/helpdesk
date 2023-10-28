import React from "react";
import TabPanel from "./TabPanel";
import TicketsTable from "./TicketsTable";
import { AppBar, Button, Tab, Tabs } from "@material-ui/core";
import { Link, Switch, Route } from "react-router-dom";
import { withRouter } from "react-router";
import TicketInfoWithRouter from "./TicketInfo";
import { ALL_TICKETS, MY_TICKETS } from "../constants/mockTickets";
import axios from "axios";
import CommentsTable from "./CommentsTable";
import FeedbackCreationPageWithRouter from "./FeedbackCreationPage";


function a11yProps(index) {
    return {
        id: `full-width-tab-${index}`,
        "aria-controls": `full-width-tabpanel-${index}`,
    };
}
class MainPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            myTicketsPage: 1,
            allTicketsPage: 1,
            tabValue: 0,
            myTickets: [],
            allTickets: [],
            keyword: "",
            field: "",
            previousSortedColumn: "",
            previousColumnCounter: 0,
            viewColumn: ""
        };
    };

    handleLogout = () => {
        localStorage.removeItem("token");
        this.props.authCallback(false);

        this.props.history.push("/");

        // put logout logic here
        // console.log("Logout");
    };

    componentDidMount() {
        // put requests for tickets here
        this.updateTickets();
    }

    updateTickets() {
        const self = this;

        const tabValue = this.state.tabValue;

        const token = localStorage.getItem('token');
        const url = 'http://localhost:8080/api/v1/tickets';
        let config = {
            headers: {
                'Authorization': token
            },
            params: {}
        }

        if (this.state.keyword != "") {
            config.params = {
                ...config.params,
                keyword: this.state.keyword
            }
        }

        if (this.state.field != "") {
            config.params = {
                ...config.params,
                field: this.state.field
            }
        }

        if (tabValue === 1) {
            config.params = {
                ...config.params,
                page: this.state.allTicketsPage,
                isPersonal: false
            }

            axios.get(url, config).then(response => {
                this.setState({ allTickets: response.data })
            }).catch(function (error) {
                if (error.response) {
                    if (error.response.status === 403) {
                        self.handleLogout();
                    }
                }
            });
        } else {
            config.params = {
                ...config.params,
                page: this.state.myTicketsPage,
                isPersonal: true
            }

            axios.get(url, config).then(response => {
                this.setState({ myTickets: response.data })
            }).catch(function (error) {
                if (error.response) {
                    if (error.response.status === 403) {
                        self.handleLogout();
                    }
                }
            })
        }
    };

    handleTabChange = (event, value) => {
        this.setState({
            tabValue: value,
            filteredTickets: [],
            myTicketsPage: 1,
            allTicketsPage: 1
        }, function () {
            this.updateTickets();
        })
    };

    handleSearchTicket = (event) => {
        this.setState({
            keyword: event.target.value,
            myTicketsPage: 1,
            allTicketsPage: 1
        }, function () {
            this.updateTickets();
        });
    };

    handleSortingTickets = (column) => {
        // put sort request here

        let viewColumn;

        if (column === "desiredResolutionDate") {
            column = "desired_date";
            viewColumn = "Desired date"
        } else {
            viewColumn = column
        }
        // this.setState({  });

        let sortingField;
        if (this.state.previousSortedColumn === column) {
            if (this.state.previousColumnCounter === 0) {
                sortingField = column.toUpperCase();
                this.setState({ previousColumnCounter: 1 });
            } else if (this.state.previousColumnCounter === 1) {
                sortingField = column.toUpperCase() + "_DESC";
                viewColumn = viewColumn + " descending"
                this.setState({ previousColumnCounter: 2 });
            } else if (this.state.previousColumnCounter === 2) {
                sortingField = "DEFAULT"
                viewColumn = ""
                this.setState({ previousColumnCounter: 0 });
            }
        } else {
            sortingField = column.toUpperCase();
            this.setState({ previousColumnCounter: 1 });
        }

        this.setState({
            field: sortingField,
            previousSortedColumn: column,
            viewColumn: viewColumn
        }, function () {
            this.updateTickets();
        });
    };


    handleAllPageChangeToPrev = () => {
        if (this.state.allTicketsPage > 1) {
            this.setState({ allTicketsPage: this.state.allTicketsPage - 1 }, function () {
                this.updateTickets();
            });
        }
    };

    handleAllPageChangeToNext = () => {
        this.setState({ allTicketsPage: this.state.allTicketsPage + 1 }, function () {
            this.updateTickets();
        });
    };

    handlePersonalPageChangeToPrev = () => {
        if (this.state.myTicketsPage > 1) {
            this.setState({ myTicketsPage: this.state.myTicketsPage - 1 }, function () {
                this.updateTickets();
            });
        }
    };

    handlePersonalPageChangeToNext = () => {
        this.setState({ myTicketsPage: this.state.myTicketsPage + 1 }, function () {
            this.updateTickets();
        });

    };

    updateActions = (token) => {
        const self = this;
        const { ticketId } = this.props.match.params;
        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/action"
        const config = {
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            this.setState({
                actions: response.data
            });
        })
    }

    render() {
        const { allTickets, myTickets, tabValue } = this.state;
        const { path } = this.props.match;
        const { handleSearchTicket } = this;

        let viewCol;
        if (this.state.viewColumn != "") {
            viewCol = "Sorting by " + this.state.viewColumn;
        }

        return (
            <>
                <Switch>
                    <Route exact path={path}>
                        <div className="buttons-container">
                            <Button
                                component={Link}
                                to="/create-ticket"
                                onClick={this.handleCreate}
                                variant="contained"
                                color="primary"
                            >
                                Create Ticket
                            </Button>
                            <Button
                                component={Link}
                                // to="/"
                                onClick={this.handleLogout}
                                variant="contained"
                                color="secondary"
                            >
                                Logout
                            </Button>
                        </div>
                        <div className="table-container">
                            <AppBar position="static">
                                <Tabs
                                    variant="fullWidth"
                                    onChange={this.handleTabChange}
                                    value={tabValue}
                                >
                                    <Tab label="My tickets" {...a11yProps(0)} />
                                    <Tab label="All tickets" {...a11yProps(1)} />
                                </Tabs>
                                <TabPanel value={tabValue} index={0}>
                                    <TicketsTable
                                        searchCallback={handleSearchTicket}
                                        sortingCallback={this.handleSortingTickets}
                                        tickets={
                                            myTickets
                                        }
                                    />
                                    <div class="table-div">
                                        <Button onClick={this.handlePersonalPageChangeToPrev} variant="contained"  >
                                            Previous page
                                        </Button>
                                        <Button onClick={this.handlePersonalPageChangeToNext} variant="contained"  >
                                            Next page
                                        </Button>
                                        <Button color="inherit">
                                            Page {this.state.myTicketsPage}
                                        </Button>
                                        <Button color="inherit">
                                            {viewCol}
                                        </Button>
                                    </div>
                                </TabPanel>
                                <TabPanel value={tabValue} index={1}>
                                    <TicketsTable
                                        searchCallback={handleSearchTicket}
                                        sortingCallback={this.handleSortingTickets}
                                        tickets={
                                            allTickets
                                        }
                                    />
                                    <div class="table-div">
                                        <Button onClick={this.handleAllPageChangeToPrev} variant="contained"  >
                                            Previous page
                                        </Button>
                                        <Button onClick={this.handleAllPageChangeToNext} variant="contained"  >
                                            Next page
                                        </Button>
                                        <Button color="inherit">
                                            Page {this.state.allTicketsPage}
                                        </Button>
                                        <Button color="inherit">
                                            {viewCol}
                                        </Button>
                                    </div>
                                </TabPanel>
                            </AppBar>
                        </div>
                    </Route>
                    <Route path={`${path}/:ticketId`}>
                        <TicketInfoWithRouter />
                    </Route>
                    {/* <Route exact path={`${path}/:ticketId/feedback`}>
                        <FeedbackPageWithRouter />
                    </Route> */}
                </Switch>
            </>
        );
    }
}

const MainPageWithRouter = withRouter(MainPage);
export default MainPageWithRouter;
