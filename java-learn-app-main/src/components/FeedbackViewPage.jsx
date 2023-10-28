import { Typography, Link, Button } from "@material-ui/core";
import React from "react";
import axios from "axios";
import { withRouter } from "react-router";

class FeedbackViewPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            rateValue: "",
            commentValue: ""
        };
    };

    componentDidMount() {
        const id = this.props.id;
        const url = `http://localhost:8080/api/v1/tickets/${id}/feedback`;
        const token = localStorage.getItem('token');

        const config = {
            headers: {
                'Authorization': token,
            }
        }

        axios.get(url, config).then(response => {
            this.setState({
                rateValue: response.data.rate,
                commentValue: response.data.comment
            })
        })
    }

    handleQuit = (id) => {
        this.props.history.push(`/main-page/${id}`)
    }

    render() {
        const id = this.props.id;
        const name = this.props.name;

        return (
            <div>
                <div className="ticket-creation-form-container" >
                    <header className="ticket-creation-form-container__navigation-container">
                        <Button onClick={() => this.handleQuit(id)} variant="contained">
                            Back
                        </Button>
                    </header>
                    <div className="ticket-creation-form-container__title">
                        <Typography display="block" variant="h4">{`Ticket(${id}) - ${name}`}</Typography>
                    </div>
                    <div className="ticket-creation-form-container__title">
                        <Typography display="block" variant="h6">Feedback</Typography>
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
                        <Typography display="block" variant="h6">
                            Rate: {this.state.rateValue}
                        </Typography>
                    </div>
                    <div className="inputs-section">
                        <Typography display="block" variant="h6">
                            Comment: {this.state.commentValue}
                        </Typography>
                    </div>

                </div>
            </div>
        )
    }

};

const FeedbackViewPageWithRouter = withRouter(FeedbackViewPage);
export default FeedbackViewPageWithRouter;

