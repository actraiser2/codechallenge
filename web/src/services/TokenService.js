import { OAuth2Client } from '@badgateway/oauth2-client';


class TokenService {
    static IDENTITY_SERVER_URL = "http://localhost:9090"
	static CLIENT_ID = "caixabank_dev"
	static CLIENT_SECRET = "xaH5SvD4IZvCfER0mhRZ2Vi2Rdk3yWKb"

    static getClient(){
        let client = new OAuth2Client({
            clientId: TokenService.CLIENT_ID,
            clientSecret: TokenService.CLIENT_SECRET,
            server: TokenService.IDENTITY_SERVER_URL,
            authenticationMethod: "client_secret_post",
            discoveryEndpoint: "/realms/josemi/.well-known/openid-configuration"
        })
        return client;
    };
    
}

export default TokenService