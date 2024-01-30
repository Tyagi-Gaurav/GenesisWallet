def get_vpc_for(client, env):
    print ("Get VPCs")
    response = client.describe_vpcs(
        Filters=[
            {
                'Name': 'tag:Environment',
                'Values': [
                    env,
                ]
            },
        ])
    print (response)
    if (response["Vpcs"]):
        return response
    else:
        print ("No Vpcs Found")
        return None

def vpc_id_from(vpcs):
    return vpcs["Vpcs"][0]["VpcId"]