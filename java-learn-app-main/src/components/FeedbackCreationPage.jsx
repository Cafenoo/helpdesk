import React from "react";
import {
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
} from "@material-ui/core";
import TabPanel from "./TabPanel";
import TicketsTable from "./TicketsTable";
import { AppBar, Button, Tab, Tabs } from "@material-ui/core";
import { Link, Switch, Route } from "react-router-dom";
import { withRouter } from "react-router";
import TicketInfoWithRouter from "./TicketInfo";
import { ALL_TICKETS, MY_TICKETS } from "../constants/mockTickets";
import axios from "axios";
import CommentsTable from "./CommentsTable";
import {
    InputLabel,
    FormControl,
    MenuItem,
    Select,
    Input,
    TextField,
} from "@material-ui/core";

class FeedbackCreationPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            name: this.props.name,
            id: this.props.id,

            rates: [1, 2, 3, 4, 5],
            rate: 5,

            commentValue: "",

            generalError: "",
        };
    };

    handleRateChange = (event) => {
        this.setState({
            rate: event.target.value
        })
    }

    handleCommentChange = (event) => {
        this.setState({
            commentValue: event.target.value,
        });
    };

    handleSubmitTicket = () => {
        // put submit logic here
        // console.log("Submit");
        const self = this;

        const id = this.props.id;
        const url = `http://localhost:8080/api/v1/tickets/${id}/feedback`;

        const token = localStorage.getItem('token');

        const config = {
            headers: {
                'Authorization': token,
            }
        }

        let commentValue;
        if (this.state.commentValue !== "") {
            commentValue = this.state.commentValue;
        } else {
            commentValue = null;
        }

        const body = {
            rate: this.state.rate,
            comment: commentValue
        }

        axios.post(url, body, config).then(response => {
            this.props.history.push(`/main-page/${id}`)
        }).catch(function (error) {
            if (error.response) {
                if (error.response.status === 403) {
                    const errorData = error.response.data.exceptionMessage;
                    self.setState({ generalError: errorData })
                }
                // if (error.response.status === 400) {
                //     const errorData = error.response.data.exceptionMessage;
                //     let readableMessages = errorData.split(/save.ticketDto./);
                //     self.setState({ generalError: readableMessages })
                // }
                if (error.response.status === 423) {
                    const errorData = error.response.data.exceptionMessage;
                    self.setState({ generalError: errorData })
                }
            }
        })

    };

    render() {
        const id = this.props.id;
        const name = this.props.name;

        return (
            <div>
                <div className="ticket-creation-form-container" >
                    <header className="ticket-creation-form-container__navigation-container">
                        <Button component={Link} to="/main-page" variant="contained">
                            Ticket List
                    </Button>
                    </header>
                    <div className="ticket-creation-form-container__title">
                        <Typography display="block" variant="h4">{`Ticket(${id}) - ${name}`}</Typography>
                    </div>
                    <div className="ticket-creation-form-container__title">
                        <Typography display="block" variant="h6">Please, rate your satisfaction with the solution:</Typography>
                    </div>
                    <div class="ticket-error-div" >
                        <Typography display="block" style={{ color: "red" }}>
                            {/* {this.state.generalError.map(item => {
                                return (<p>
                                    {item}
                                </p>);
                            })} */}
                            {this.state.generalError}
                        </Typography>
                    </div>
                    <div className="inputs-section">
                        <FormControl variant="outlined" required>
                            <InputLabel shrink htmlFor="rate-label">
                                Rate
                    </InputLabel>
                            <Select
                                value={this.state.rate}
                                label="Rate"
                                onChange={this.handleRateChange}
                                inputProps={{
                                    name: "rate",
                                    id: "rate-label",
                                }}
                            >
                                {this.state.rates.map((item, index) => {
                                    return (
                                        <MenuItem value={item} key={index}>
                                            {item}
                                        </MenuItem>
                                    );
                                })}
                            </Select>
                        </FormControl>
                    </div>
                    <div className="inputs-section">
                        <FormControl>
                            <TextField
                                label="Comment"
                                multiline
                                rows={4}
                                variant="outlined"
                                value={this.state.commentValue}
                                className="creation-text-field creation-text-field_width680"
                                onChange={this.handleCommentChange}
                            />
                        </FormControl>
                    </div>
                    <section className="submit-button-section">
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
            </div>
        )
    }

}

const FeedbackCreationPageWithRouter = withRouter(FeedbackCreationPage);
export default FeedbackCreationPageWithRouter;