import React from "react";
import {
    InputLabel,
    FormControl,
    MenuItem,
    Select,
    Input,
} from "@material-ui/core";
import { ALL_TICKETS } from "../constants/mockTickets";
import { CATEGORIES_OPTIONS, URGENCY_OPTIONS } from "../constants/inputsValues";
import PropTypes from "prop-types";
import CommentsTable from "./CommentsTable";
import HistoryTable from "./HistoryTable";
import TabPanel from "./TabPanel";
import TicketCreationPageWithRouter from "./TicketCreationPage";
import { Link, Route, Switch } from "react-router-dom";
import { withRouter } from "react-router";
import { COMMENTS } from "../constants/mockComments";
import { HISTORY } from "../constants/mockHistory";

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

function a11yProps(index) {
    return {
        id: `full-width-tab-${index}`,
        "aria-controls": `full-width-tabpanel-${index}`,
    };
}

class TicketEditionPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            // ticketData: this.props.savedTicketData,

            categories: [],
            urgencies: [],

            categoryValue: "",
            nameValue: "",
            descriptionValue: "",
            urgencyValue: "",
            resolutionDateValue: "",
            attachmentValue: null,
            commentValue: "",

            currentAttachmentId: null,
            currentAttachmentName: "",

            generalError: [],
            nameError: "",

            getAllComments: false,
            ticketComments: []
        };
    }

    loadCategories = () => {
        const url = 'http://localhost:8080/api/v1/categories';

        const token = localStorage.getItem('token');

        const config = {
            headers: {
                'Authorization': token,
            }
        }

        axios.get(url, config).then(response => {
            this.setState({ categories: response.data })
        })
    }

    loadUrgencies = () => {
        const url = 'http://localhost:8080/api/v1/urgency';

        const token = localStorage.getItem('token');

        const config = {
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            this.setState({ urgencies: response.data });
        })
    }

    loadComments = () => {
        const { ticketId } = this.props.match.params;

        const token = localStorage.getItem('token');

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

    componentDidMount() {
        // set request for getting ticket in draft state

        this.loadCategories();

        this.loadUrgencies();

        this.loadComments();

        // const ticketFromUrl = this.props.location.pathname.split("/");
        // const ticketId = ticketFromUrl[ticketFromUrl.length - 1];
        // const ticketData = ALL_TICKETS.find((item) => item.id === +ticketId);

        // const ticketData = this.props.savedTicketData;


        // // if (ticketData != null) {

        //    // }

        const { ticketId } = this.props.match.params;
        // console.log(ticketId)

        const url = 'http://localhost:8080/api/v1/tickets/' + ticketId;

        const token = localStorage.getItem('token');

        const config = {
            headers: {
                'Authorization': token
            }
        }

        axios.get(url, config).then(response => {
            // this.setState({ urgencies: response.data });

            const ticketData = response.data
            console.log(ticketData)

            let desiredDate;
            if (ticketData.desiredResolutionDate != null) {
                const dateNumbers = ticketData.desiredResolutionDate.split(/\//);
                desiredDate = dateNumbers[2] + "-" + dateNumbers[1] + "-" + dateNumbers[0];
            } else {
                desiredDate = "";
            }

            let description;
            if (ticketData.description != null) {
                description = ticketData.description
            } else {
                description = "";
            }

            this.setState({
                nameValue: ticketData.name,
                resolutionDateValue: desiredDate,
                descriptionValue: description,
                urgencyValue: ticketData.urgency,
                categoryValue: ticketData.category.id,
                // currentAttachmentName: ticketData.attachment.name    
            });
        })

        // const commentsUrl = url + "/comments"

        // axios.get(commentsUrl, config).then(response => {
        //     if (response.data[0] != null) {
        //         this.setState({
        //             commentValue: response.data[0].text
        //         })
        //     } else {
        //         this.setState({
        //             commentValue: null
        //         })
        //     }
        // })

        const attachmentUrl = url + "/attachment"

        axios.get(attachmentUrl, config).then(response => {
            this.setState({
                currentAttachmentName: response.data.name
            })
        })

    }

    handleCategoryChange = (event) => {
        this.setState({
            categoryValue: event.target.value,
        });
    };

    handleNameChange = (event) => {
        this.setState({
            nameValue: event.target.value,
            nameError: ""
        });
    };

    handleDescriptionChange = (event) => {
        this.setState({
            descriptionValue: event.target.value,
        });
    };

    handleUrgencyChange = (event) => {
        this.setState({
            urgencyValue: event.target.value,
        });
    };

    handleResolutionDate = (event) => {
        this.setState({
            resolutionDateValue: event.target.value,
        });
    };

    handleAttachmentChange = (event) => {
        this.setState({
            attachmentValue: event.target.files[0],
        });
    };

    handleCommentChange = (event) => {
        this.setState({
            commentValue: event.target.value,
        });
    };

    handleTicketSubmission = (state) => {
        const self = this;

        const { ticketId } = this.props.match.params;

        if (this.state.nameValue == "") {
            this.setState({ nameError: "Name shouldn't be null" })
        } else {

            const url = 'http://localhost:8080/api/v1/tickets/' + ticketId;

            const token = localStorage.getItem('token');

            const config = {
                headers: {
                    'Authorization': token,
                    'Content-Type': 'multipart/form-data'
                }
            }

            const form = new FormData();
            form.append("name", this.state.nameValue);
            form.append("category.id", this.state.categoryValue);
            form.append("urgency", this.state.urgencyValue);
            form.append("state", state);
            if (this.state.descriptionValue != "") {
                form.append("description", this.state.descriptionValue);
            }
            if (this.state.commentValue != "") {
                form.append("comment.text", this.state.commentValue);
            }
            if (this.state.attachmentValue != null) {
                form.append("attachment.file", this.state.attachmentValue);
            }
            if (this.state.resolutionDateValue != "") {
                form.append("desiredResolutionDate", this.state.resolutionDateValue);
            }

            axios.put(url, form, config).then(res => {
                this.props.history.push(this.props.match.url)
            }).catch(function (error) {
                if (error.response) {
                    if (error.response.status === 403) {
                        self.handleLogout();
                    }
                    if (error.response.status === 400) {
                        const errorData = error.response.data.exceptionMessage;
                        let readableMessages = errorData.split(/edit.ticketDto./);
                        self.setState({ generalError: readableMessages })
                    }
                }
            })
        }
    }

    handleSaveDraft = () => {
        // put change of status to draft here
        // console.log("Save as draft");

        this.handleTicketSubmission("DRAFT");
    };

    handleSubmitTicket = () => {
        // put submit logic here
        // console.log("Submit");

        this.handleTicketSubmission("NEW");
    };

    handleAttachmentDelete = () => {
        if (this.state.currentAttachmentName != "") {
            const { ticketId } = this.props.match.params;
            const url = 'http://localhost:8080/api/v1/tickets/' + ticketId + "/attachment";
            const token = localStorage.getItem('token');

            const config = {
                headers: {
                    'Authorization': token,
                }
            }

            axios.delete(url, config).then(resp => {
                this.setState({
                    currentAttachmentName: ""
                })
            })
        }
    }

    handleEnterComment = (event) => {
        this.setState({
            commentValue: event.target.value,
        });
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

    addComment = () => {
        // put request for comment creation here
        const { ticketId } = this.props.match.params;

        const url = "http://localhost:8080/api/v1/tickets/" + ticketId + "/comments";

        const token = localStorage.getItem('token');

        const config = {
            params: {
                comment: this.state.commentValue
            },
            headers: {
                'Authorization': token
            }
        }

        axios.post(url, null, config).then(response => {
            this.loadComments(token)
        });

        this.setState({
            commentValue: "",
        });
    };

    render() {
        const {
            nameValue,
            attachmentValue,
            categoryValue,
            commentValue,
            descriptionValue,
            resolutionDateValue,
            urgencyValue,
        } = this.state;

        const { url } = this.props.match;

        return (
            <div className="ticket-creation-form-container" >
                <header className="ticket-creation-form-container__navigation-container">
                    <Button component={Link} to={url} variant="contained">
                        Return back
                    </Button>
                </header>
                <div className="ticket-creation-form-container__title">
                    <Typography display="block" variant="h3">
                        Edit ticket
                    </Typography>
                </div>
                <div class="ticket-error-div" >
                    <Typography display="block" style={{ color: "red" }}>
                        {this.state.generalError.map(item => {
                            return (<p>
                                {item}
                            </p>);
                        })}
                    </Typography>
                </div>
                <div className="ticket-creation-form-container__form">
                    <div className="inputs-section">
                        <div className="ticket-creation-form-container__inputs-section inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
                            <FormControl>
                                <TextField
                                    required
                                    label="Name"
                                    variant="outlined"
                                    onChange={this.handleNameChange}
                                    id="name-label"
                                    value={nameValue}
                                    helperText={this.state.nameError}
                                />
                            </FormControl>
                        </div>
                        <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
                            <FormControl variant="outlined" required>
                                <InputLabel shrink htmlFor="category-label">
                                    Category
                                </InputLabel>
                                <Select
                                    value={categoryValue}
                                    label="Category"
                                    onChange={this.handleCategoryChange}
                                    inputProps={{
                                        name: "category",
                                        id: "category-label",
                                    }}
                                >
                                    {this.state.categories.map((item, index) => {
                                        return (
                                            <MenuItem value={item.id} key={item.id}>
                                                {item.name}
                                            </MenuItem>
                                        );
                                    })}
                                </Select>
                            </FormControl>
                        </div>
                        <div className="inputs-section__ticket-creation-input ticket-creation-input">
                            <FormControl variant="outlined" required>
                                <InputLabel shrink htmlFor="urgency-label">
                                    Urgency
                                 </InputLabel>
                                <Select
                                    value={urgencyValue}
                                    label="Urgency"
                                    onChange={this.handleUrgencyChange}
                                    className={"ticket-creation-input_width200"}
                                    inputProps={{
                                        name: "urgency",
                                        id: "urgency-label",
                                    }}
                                >
                                    {this.state.urgencies.map((item, index) => {
                                        return (
                                            <MenuItem value={item} key={index}>
                                                {item}
                                            </MenuItem>
                                        );
                                    })}
                                </Select>
                            </FormControl>
                        </div>
                    </div>
                    <div className="inputs-section-attachment">
                        <div className="inputs-section__ticket-creation-input ticket-creation-input ticket-creation-input_width200">
                            <FormControl>
                                <InputLabel shrink htmlFor="urgency-label">
                                    Desired resolution date
                                 </InputLabel>
                                <TextField
                                    onChange={this.handleResolutionDate}
                                    label="Desired resolution date"
                                    type="date"
                                    id="resolution-date"
                                    value={resolutionDateValue}
                                    InputLabelProps={{
                                        shrink: true,
                                    }}
                                />
                            </FormControl>
                        </div>
                        <div className="ticket-creation-input">
                            <FormControl>
                                <Typography variant="caption">Edit attachment</Typography>
                                <TextField
                                    type="file"
                                    variant="outlined"
                                    onChange={this.handleAttachmentChange}
                                />
                            </FormControl>
                            <p>
                                Current attachment: {this.state.currentAttachmentName || <span class="not-assigned-stats">Not assigned</span>}
                            </p>
                            <Button variant="contained" onClick={this.handleAttachmentDelete}>
                                Delete attachment
                                </Button>
                        </div>
                    </div>

                    <div className="inputs-section">
                        <FormControl>
                            <TextField
                                label="Description"
                                multiline
                                rows={4}
                                variant="outlined"
                                value={descriptionValue}
                                className="creation-text-field creation-text-field_width680"
                                onChange={this.handleDescriptionChange}
                            />
                        </FormControl>
                    </div>
                    <div className="inputs-section">
                        <FormControl>
                            <TextField
                                label="Comment"
                                multiline
                                rows={4}
                                variant="outlined"
                                value={commentValue}
                                className="creation-text-field creation-text-field_width680"
                                onChange={this.handleCommentChange}
                            />
                        </FormControl>
                    </div>
                    <div className="submit-button-section">
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
                    <br />
                    <section className="submit-button-section">
                        <Button
                            variant="contained"
                            onClick={this.handleSaveDraft}
                        // to={url}
                        // component={Link}
                        >
                            Save as Draft
                        </Button>
                        <Button
                            // component={Link}
                            // to={url}
                            variant="contained"
                            onClick={this.handleSubmitTicket}
                            color="primary"
                        >
                            Submit
                         </Button>
                    </section>
                    <div className="ticket-data-container__comments-section comments-section creation-text-field_width680">
                        <div className="">
                            <Tabs
                                variant="fullWidth"
                                value={0}
                                indicatorColor="primary"
                                textColor="primary"
                            >
                                <Tab label="Comments" {...a11yProps(1)} />
                            </Tabs>
                            <TabPanel value={0} index={0}>
                                <CommentsTable comments={this.state.ticketComments} />
                            </TabPanel>
                        </div>
                    </div>

                    {/* <TextField
                            label="Enter a comment"
                            multiline
                            rows={4}
                            value={commentValue}
                            variant="filled"
                            className="comment-text-field"
                            onChange={this.handleEnterComment}
                        /> */}


                </div>
            </div >
        );
    }
}

const TicketEditionPageWithRouter = withRouter(TicketEditionPage);
export default TicketEditionPageWithRouter;
