import TokenService from './TokenService'

class ApiClient {
	static BACKEND_URL = "http://localhost:8080/api/v1"
	
	
	static getAccounts(){
		var client = TokenService.getClient();
		var token = client.clientCredentials({scope: ['read:accounts']})

		return token.then(r => {
			var accessToken = r.accessToken;
			return accessToken;
		}).
		then(token => {
			var headers = {
				"authorization": "Bearer " + token
			};
			
			return fetch(ApiClient.BACKEND_URL + "/accounts", {
				"headers": headers
			})
		})
		
		
	}

	static getAccount(accountId){
		var client = TokenService.getClient();
		var token = client.clientCredentials({scope: ['read:accounts']})

		return token.then(r => {
			var accessToken = r.accessToken;
			return accessToken;
		}).
		then(token => {
			var headers = {
				"authorization": "Bearer " + token
			};
			
			return fetch(ApiClient.BACKEND_URL + "/accounts/" + accountId, {
				"headers": headers
			})
		})
		
	}
}

export default ApiClient