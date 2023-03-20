
# Find all resources with a given tag and delete them.
# Get VPC id for a given environment
export ENV=dev
echo "Fetch VPC ID for $ENV"
vpc_id=$(aws ec2 describe-vpcs --filters Name=tag:Environment,Values=$ENV | jq -r '.Vpcs[].VpcId')
echo "VPC ID is $vpc_id"
# Describe Elastic IPs
addresses=$(aws ec2 describe-addresses)
echo $addresses
#for row in $addresses; do
#  echo "Found addresses $row"
#done
# Disassociate Elastic IPs
#vpc_id=$(aws ec2 describe-vpcs --filters Name=tag:Environment,Values=$ENV | jq -r '.Vpcs[].VpcId')
# Delete network interfaces
#nids=$(aws ec2 describe-network-interfaces --filters vpc-id=$vpc_id)
#echo "Delete Network interfaces: $nids"


# Delete VPC
#aws ecs describe-clusters


#sg=$(aws ec2 describe-security-groups --filters Name=tag:Environment,Values=$ENV | jq -r '.SecurityGroups[].GroupId')
#for row in $sg; do
#  aws ec2 delete-security-group --group-id $row
#done
# Delete S3 bucket
# Delete Route 53 resources
# Delete security groups
# Delete subnets
# Delete VPC