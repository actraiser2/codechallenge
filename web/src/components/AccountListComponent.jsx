import { Component } from "react";
import { Link } from "react-router-dom"

import AccountComponent from './AccountComponent'              
import ApiClient from "../services/ApiClient";


class AccountListComponent extends Component{
	constructor(props){
		super(props)
		this.state = {
			accounts:[],
			buttonText: 'Click me to refresh'
		}
		console.log("Created instance ApplicationComponent")
	}
	
	render(){
		return(
			<div className="container">
				<button className="btn btn-dark my-2" onClick={(e) => this.handleClickRefresh(e)}>
					{this.state.buttonText}
				</button>


				<table style={{width: "600px"}} className="table table-bordered table-striped">
					<thead>
						<tr>
							<th>AccountId</th>
							<th>AccountName</th>
							<th>Iban</th>
							<th>Balance</th>
						</tr>
					</thead>
					<tbody>
					{   this.state.accounts.map(a => {
						return (
							<tr key={a["account-id"]}>
								<td>
									<Link to={"/accountDetail/" + a["account-id"]}>
										{a["account-id"]}
									</Link>
								</td>
								<td>{a["account-name"]}</td>
								<td>{a["iban"]}</td>
								<td>{a["balance"]}</td>
							</tr>
					    )
					})
					}
					</tbody>
					
				
				</table>
			</div>
			
		)
	}

	componentDidMount(){
		console.log("Componend Did Mount")
		this.getAccounts();
	}

	getAccounts(){
		ApiClient.getAccounts().then(r => {
			r.json().then(data => {
				console.log("Data:" + JSON.stringify(data));
				this.setState({
					accounts: data["account-list"]
				})
			})
			
		})
	}

	handleClickRefresh(event){
		console.log("Event triggered:" + event)
		this.getAccounts()
	}

	handleClickDetail(accountId){
		console.log("Account detail for " + accountId)
	} 
}

export default AccountListComponent