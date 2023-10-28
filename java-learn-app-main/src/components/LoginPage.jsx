import React from "react";
import { Button, TextField, Typography } from "@material-ui/core";
import axios from "axios";

class LoginPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            emailValue: "",
            passwordValue: "",
            emailHelperText: "",
            passwordHelperText: "",
            error: ""
        };
    }

    componentDidMount() {
        let token = localStorage.getItem('token');
        if (token === null) {
            this.props.authCallback(false);
        }
    }

    handleEmailChange = (event) => {
        this.setState({ emailValue: event.target.value });
    };

    handlePasswordChange = (event) => {
        this.setState({ passwordValue: event.target.value });
    };

    handleClickAuth = () => {
        // put authorization logic here

        this.setState({ error: "" });

        if (this.state.emailValue === "") {
            this.setState({ emailHelperText: "Please fill out the required field." })
        } else {
            this.setState({ emailHelperText: "" })
        }

        if (this.state.passwordValue === "") {
            this.setState({ passwordHelperText: "Please fill out the required field." })
        } else {
            this.setState({ passwordHelperText: "" })
        }

        if (this.state.emailValue !== "" || this.state.passwordValue !== "") {
            const url = 'http://localhost:8080/login';

            const body = {
                'email': this.state.emailValue,
                'password': this.state.passwordValue
            }

            axios.post(url, body).then(response => {
                localStorage.setItem('token', response.data);
                this.props.authCallback(true);
            }).catch(error => {
                this.setState({ error: "Please make sure you are using a valid email or password" });
            });
        }
    }

    render() {
        return (
            <div className="container">
                <div className="container__title-wrapper">
                    <Typography component="h2" variant="h3">
                        Login to the Help Desk
                    </Typography>
                </div>
                <div className="container__from-wrapper">
                    <form>
                        <div className="container__inputs-wrapper">
                            <div className="form__input-wrapper">
                                <TextField
                                    onChange={this.handleEmailChange}
                                    label="Email"
                                    variant="outlined"
                                    placeholder="Email"
                                    helperText={this.state.emailHelperText}
                                />
                            </div>
                            <div className="form__input-wrapper">
                                <TextField
                                    onChange={this.handlePasswordChange}
                                    label="Password"
                                    variant="outlined"
                                    type="password"
                                    placeholder="Password"
                                    helperText={this.state.passwordHelperText}
                                />
                            </div>
                        </div>
                    </form>
                </div>
                <div>
                    <b style={{ color: "red" }}>{this.state.error}</b>
                </div>
                <div className="container__button-wrapper">
                    <Button
                        size="large"
                        variant="contained"
                        color="primary"
                        onClick={this.handleClickAuth}
                    >
                        Enter
                    </Button>
                </div>
            </div>
        );
    }
};

export default LoginPage;