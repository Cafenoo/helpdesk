import React from "react";
import {
    Button,
    InputLabel,
    FormControl,
    MenuItem,
    Select,
    TextField,
    Typography,
    Input,
} from "@material-ui/core";
import { Link, withRouter } from "react-router-dom";
import { ALL_TICKETS } from "../constants/mockTickets";
import { CATEGORIES_OPTIONS, URGENCY_OPTIONS } from "../constants/inputsValues";
import axios from "axios";

class TicketCreationPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            categories: [],
            urgencies: [],

            categoryValue: "",
            nameValue: "",
            descriptionValue: "",
            urgencyValue: "",
            resolutionDateValue: "",
            attachmentValue: null,
            commentValue: "",

            generalError: [],
            nameError: ""
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
        }).then(response => {
            this.setState({ categoryValue: this.state.categories[0].id });
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
        }).then(response => {
            this.setState({ urgencyValue: this.state.urgencies[0] });
        })
    }

    componentDidMount() {
        // set request for getting ticket in draft state

        const ticketFromUrl = this.props.location.pathname.split("/");
        const ticketId = ticketFromUrl[ticketFromUrl.length - 1];
        const ticketData = ALL_TICKETS.find((item) => item.id === +ticketId);

        if (ticketData) {
            this.setState({
                nameValue: ticketData.name,
                resolutionDateValue: ticketData.date,
                commentValue: ticketData.comment,
                descriptionValue: ticketData.description,
                urgencyValue: ticketData.urgency,
                categoryValue: ticketData.category
            });
        }

        this.loadCategories();

        this.loadUrgencies();
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

        if (this.state.nameValue == "") {
            this.setState({ nameError: "Name shouldn't be null" })
        } else {

            const url = 'http://localhost:8080/api/v1/tickets';

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

            axios.post(url, form, config).then(res => {
                this.props.history.push('/main-page')
            }).catch(function (error) {
                if (error.response) {
                    if (error.response.status === 403) {
                        const errorData = error.response.data.exceptionMessage;
                        // let readableMessages = errorData.split(/save.ticketDto./);
                        self.setState({ generalError: errorData })
                    }
                    if (error.response.status === 400) {
                        const errorData = error.response.data.exceptionMessage;
                        // let readableMessages = errorData.split(/save.ticketDto./);
                        self.setState({ generalError: errorData })
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

        return (
            <div className="ticket-creation-form-container" >
                <header className="ticket-creation-form-container__navigation-container">
                    <Button component={Link} to="/main-page" variant="contained">
                        Ticket List
                    </Button>
                </header>
                <div className="ticket-creation-form-container__title">
                    <Typography display="block" variant="h3">
                        Create new ticket
                    </Typography>
                </div>
                <div class="ticket-error-div" >
                    <Typography display="block" style={{ color: "red" }}>
                        {this.state.generalError}
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
                                <Typography variant="caption">Add attachment</Typography>
                                <TextField
                                    type="file"
                                    variant="outlined"
                                    onChange={this.handleAttachmentChange}
                                />
                            </FormControl>
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
                    <section className="submit-button-section">
                        <Button variant="contained" onClick={this.handleSaveDraft}>
                            Save as Draft
                        </Button>
                        <Button
                            // component={Link}
                            // to="/main-page"
                            variant="contained"
                            onClick={this.handleSubmitTicket}
                            color="primary"
                        >
                            Submit
                         </Button>
                    </section>
                </div>
            </div >
        );
    }
}

const TicketCreationPageWithRouter = withRouter(TicketCreationPage);
export default TicketCreationPageWithRouter;
