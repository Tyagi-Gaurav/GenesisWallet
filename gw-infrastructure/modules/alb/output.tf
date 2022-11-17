output "dns_name" {
  value = aws_alb.alb.dns_name
}

output "alb_arn" {
  value = aws_alb.alb.arn
}

output "zone_id" {
  value = aws_alb.alb.zone_id
}

#output "http_listener_arn" {
#  value = aws_alb_listener.alb_http.arn
#}

#output "https_listener_arn" {
#  value = aws_alb_listener.alb_https.arn
#}

output "alb_security_group_id" {
  value = aws_security_group.alb_security_group.id
}