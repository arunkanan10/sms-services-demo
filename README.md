A spring boot application [micro service API server] that serves as fulfillment for SMS Services

**Features:**

__Inbound Service__ - When text is STOP or STOP\n or STOP\r or STOP\r\n.
                  The ‘from’ and ‘to’ pair will be stored in cache as a unique entry and it will expire after 4 hours.

__Outbound Service__ - It will allow only 50 API requests using the same ‘from’ number in
                   24 hours from the first use of the ‘from’ number and reset counter after 24 hours.

The micro services will be protected by token header and which is used for authentication. Below are example headers.
    - [{"key":"username","value":"test1","description":""}]
    - [{"key":"authId","value":"xxxxxxxxxx","description":""}]

**API /inbound/sms/ :**

__Sample Request:__
{
	"from": "3253280329",
	"to": "4924195509198",
	"text": "STOP\n"
}


**API /outbound/sms/ :**

__Sample Request:__

{
	"from": "3253280329",
	"to": "4924195509198",
	"text": "STOP\n"
}
