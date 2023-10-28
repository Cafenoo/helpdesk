import React from "react";
import PropTypes from "prop-types";
import CommentsTable from "./CommentsTable";
import HistoryTable from "./HistoryTable";
import TabPanel from "./TabPanel";
import TicketCreationPageWithRouter from "./TicketCreationPage";
import { Link, Route, Switch } from "react-router-dom";
import { withRouter } from "react-router";
import { ALL_TICKETS } from "../constants/mockTickets";
import { COMMENTS } from "../constants/mockComments";
import { HISTORY } from "../constants/mockHistory";
import FeedbackCreationPageWithRouter from "./FeedbackCreationPage";
import FeedbackViewPageWithRouter from "./FeedbackViewPage";
import {
    Button,
    ButtonGroup,
    Paper,
    Tab,
    Tabs,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableRow,
    Typography,
    TextField,
} from "@material-ui/core";
import axios from "axios";
import TicketEditionPageWithRouter from "./TicketEditionPage";

function a11yProps(index) {
    return {
        id: `full-width-tab-${index}`,
        "aria-controls": `full-width-tabpanel-${index}`,
    };
}

class TicketInfo extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            commentValue: "",
            tabValue: 0,
            ticketComments: [],
            getAllComments: false,
            ticketHistory: [],
            getAllHistory: false,
            // currentUser: {
            //     name: "",
            //     id: "",
            // },
            feedback: null,
            ticketData: {
                id: "",
                name: "",
                createdOn: "",
                category: {},
                state: "",
                urgency: "",
                desiredResolutionDate: "",
                owner: {},
                approver: {
                    firstName: null,
                    lastName: null
                },
                assignee: {
                    firstName: null,
                    lastName: null
                },
                attachment: "",
                description: "",
                actions: []
            },
        };
    }

    componentDidMount() {
        // get required ticket by id
        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId;

        const token = localStorage.getItem('token');

        const config = {
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            let approverFirstName;
            let approverLastName;
            if (response.data.approver != null) {
                approverFirstName = response.data.approver.firstName;
                approverLastName = response.data.approver.lastName;
            } else {
                approverFirstName = null;
                approverLastName = null;
            }

            let assigneeFirstName
            let assigneeLastName
            if (response.data.assignee != null) {
                assigneeFirstName = response.data.assignee.firstName
                assigneeLastName = response.data.assignee.lastName;
            } else {
                assigneeFirstName = null;
                assigneeLastName = null;
            }

            this.setState({
                ticketData: {
                    ...this.state.ticketData,
                    id: response.data.id,
                    createdOn: response.data.createdOn,
                    desiredResolutionDate: response.data.desiredResolutionDate,
                    name: response.data.name,
                    state: response.data.state,
                    urgency: response.data.urgency,
                    category: response.data.category,
                    description: response.data.description,
                    owner: response.data.owner,
                    approver: {
                        firstName: approverFirstName,
                        lastName: approverLastName
                    },
                    assignee: {
                        firstName: assigneeFirstName,
                        lastName: assigneeLastName
                    },
                    actions: response.data.actions
                }
            })
        })

        this.updateAttachments(token);

        this.updateHistory(token);

        this.updateComments(token);

        this.updateFeedbackState(token);

        // this.updateActions(token);
    }

    // updateActions = (token) => {
    //     const self = this;
    //     const { ticketId } = this.props.match.params;
    //     const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/action"
    //     const config = {
    //         headers: {
    //             'Authorization': token
    //         }
    //     }

    //     axios.get(url, config).then(response => {
    //         this.setState({
    //             actions: response.data
    //         });
    //     })
    // }

    updateFeedbackState = (token) => {
        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/feedback"

        const config = {
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            this.setState({ feedback: response.data })
        })
    }

    updateAttachments = (token) => {
        const self = this

        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/attachment"

        const config = {
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            this.setState({
                ticketData: {
                    ...this.state.ticketData,
                    attachment: response.data
                }
            });
        }).catch(function (error) {
            if (error.response) {
                if (error.response.status === 404) {
                    self.setState({
                        ticketData: {
                            ...self.state.ticketData,
                            attachment: ""
                        }
                    });
                }
            }
        });
    }

    updateComments = (token) => {
        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/comments"

        const config = {
            params: {
                "doGetAll": this.state.getAllComments
            },
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            this.setState({ ticketComments: response.data })
        })
    }

    updateHistory = (token) => {
        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/history";

        const config = {
            params: {
                "doGetAll": this.state.getAllHistory
            },
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            this.setState({ ticketHistory: response.data })
        })
    }

    handleTabChange = (event, value) => {
        this.setState({
            tabValue: value,
        });
    };

    handleEnterComment = (event) => {
        this.setState({
            commentValue: event.target.value,
        });
    };

    addComment = () => {
        // put request for comment creation here
        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/comments";

        const token = localStorage.getItem('token');

        const config = {
            // params: {
            //     comment: this.state.commentValue
            // },
            headers: {
                'Authorization': token
            }
        }

        const data = {
            text: this.state.commentValue
        }

        axios.post(url, data, config).then(response => {
            this.updateComments(token)
        });

        this.setState({
            commentValue: "",
        });
    };

    handleSubmitTicket = () => {
        // set ticket status to 'submitted'
        console.log("SUBMIT ticket");
    };

    handleEditTicket = () => {
        console.log("EDIT ticket");
    };

    handleCancelTicket = () => {
        // set ticket status to 'canceled' status
        console.log("CANCEL ticket");
    };

    handleCommentPagination = () => {
        if (this.state.getAllComments === false) {
            this.state.getAllComments = true;
        } else {
            this.state.getAllComments = false;
        }

        const token = localStorage.getItem('token');

        this.updateComments(token);
    }

    handleHistoryPagination = () => {
        if (this.state.getAllHistory === false) {
            this.state.getAllHistory = true;
        } else {
            this.state.getAllHistory = false;
        }

        const token = localStorage.getItem('token');

        this.updateHistory(token);
    }

    handleAttachmentDownload = () => {
        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/attachment/download";

        const token = localStorage.getItem('token');

        const fileDownload = require('js-file-download');

        const config = {
            responseType: 'blob',
            headers: {
                'Authorization': token,
            }
        }

        axios.get(url, config).then(response => {
            fileDownload(response.data, this.state.ticketData.attachment.name);
        });
    }

    handleActionSubmission = (action) => {
        const self = this;
        const { ticketId } = this.props.match.params;
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
                ticketData: {
                    ...this.state.ticketData,
                    actions: response.data
                }
            });
            this.componentDidMount();
        })
    }

    render() {
        const {
            approver,
            id,
            name,
            createdOn,
            category,
            state,
            urgency,
            desiredResolutionDate,
            owner,
            assignee,
            attachment,
            description,
            actions
        } = this.state.ticketData;

        const { commentValue, tabValue, ticketComments, ticketHistory } =
            this.state;

        const { url } = this.props.match;

        const { handleCancelTicket, handleEditTicket, handleSubmitTicket } = this;

        const notAssignedView = <span class="not-assigned-stats">Not assigned</span>;

        let attachmentView;
        if (attachment.name != null) {
            attachmentView = <Link onClick={this.handleAttachmentDownload}>{attachment.name}</Link>
        } else {
            attachmentView = notAssignedView
        }

        let feedbackComponent;
        if (this.state.feedback != null) {
            feedbackComponent = <FeedbackViewPageWithRouter id={this.state.ticketData.id} name={this.state.ticketData.name} />
        } else {
            feedbackComponent = <FeedbackCreationPageWithRouter id={this.state.ticketData.id} name={this.state.ticketData.name} />
        }

        return (
            <Switch>
                <Route exact path={url}>
                    <div className="ticket-data-container">
                        <div className={"ticket-data-container__back-button back-button"}>
                            <Button component={Link} to="/main-page" variant="contained">
                                Ticket list
                            </Button>
                            {state === "DRAFT" && (
                                <Button component={Link}
                                    to={`${url}/edit`}
                                    variant="contained"
                                    color="secondary"
                                // onClick={handleEditTicket}
                                >
                                    Edit ticket
                                </Button>
                            )}
                        </div>
                        <div className="ticket-data-container__title">
                            <Typography variant="h4">{`Ticket(${id}) - ${name}`}</Typography>
                        </div>
                        <div className="ticket-data-container__info">
                            <TableContainer className="ticket-table" component={Paper}>
                                <Table>
                                    <TableBody>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Created on:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {createdOn}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Category:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {category.name}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Status:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {state}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Urgency:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {urgency}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Desired Resolution Date:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {desiredResolutionDate || notAssignedView}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Owner:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {owner.firstName + " " + owner.lastName}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Approver:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {(approver.firstName && " " && approver.lastName) || notAssignedView}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Assignee:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {assignee.firstName && " " && assignee.lastName || notAssignedView}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Attachments:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {attachmentView}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                        <TableRow>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    Description:
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                <Typography align="left" variant="subtitle1">
                                                    {description || notAssignedView}
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </div>
                        {actions != [] && (
                            <div className="ticket-data-container__button-section">
                                <ButtonGroup variant="contained" color="primary">
                                    {actions.map(item => {
                                        return (<Button onClick={() => this.handleActionSubmission(item)}>
                                            {item}
                                        </Button>);
                                    })}
                                </ButtonGroup>
                            </div>
                        )}
                        {state == "DONE" && (
                            <div className="ticket-data-container__button-section">
                                <ButtonGroup variant="contained" color="primary">
                                    <Button
                                        component={Link}
                                        to={`${url}/feedback`}>
                                        Feedback
                                    </Button>
                                </ButtonGroup>
                            </div>
                        )}
                        <div className="ticket-data-container__comments-section comments-section">
                            <div className="">
                                <Tabs
                                    variant="fullWidth"
                                    onChange={this.handleTabChange}
                                    value={tabValue}
                                    indicatorColor="primary"
                                    textColor="primary"
                                >
                                    <Tab label="History" {...a11yProps(0)} />
                                    <Tab label="Comments" {...a11yProps(1)} />
                                </Tabs>
                                <TabPanel value={tabValue} index={0}>
                                    <HistoryTable history={ticketHistory} />
                                    <br />
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={this.handleHistoryPagination}
                                    >
                                        Toggle showing all
                                    </Button>
                                </TabPanel>
                                <TabPanel value={tabValue} index={1}>
                                    <CommentsTable comments={ticketComments} />
                                </TabPanel>
                            </div>
                        </div>
                        {tabValue && (
                            <div className="ticket-data-container__enter-comment-section enter-comment-section">
                                <TextField
                                    label="Enter a comment"
                                    multiline
                                    rows={4}
                                    value={commentValue}
                                    variant="filled"
                                    className="comment-text-field"
                                    onChange={this.handleEnterComment}
                                />
                                <div className="enter-comment-section__add-comment-button">
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={this.addComment}
                                    >
                                        Add Comment
                                    </Button>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        onClick={this.handleCommentPagination}
                                    >
                                        Toggle showing all
                                    </Button>
                                </div>
                            </div>
                        )}
                    </div>
                </Route>
                {/* <Route path="/create-ticket/:ticketId">
                    <TicketCreationPageWithRouter />
                </Route> */}
                {/* <Route */}
                {/* // path={url + "/edit"} */}
                {/* // path={`${url}/:ticketId`} */}
                {/* > */}
                {/* <TicketEditionPageWithRouter
                    // currentUrl={url} 
                    // savedTicketData={this.state.ticketData} 
                    /> */}
                {/* </Route> */}
                <Route exact path={`${url}/feedback`}>
                    {feedbackComponent}
                </Route>
                <Route
                // path={`${url}/edit`}
                >
                    <TicketEditionPageWithRouter />
                </Route>
            </Switch >
        );
    }
}

TicketInfo.propTypes = {
    match: PropTypes.object,
};

const TicketInfoWithRouter = withRouter(TicketInfo);
export default TicketInfoWithRouter;
