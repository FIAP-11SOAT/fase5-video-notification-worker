# IAM Policy Document para Trust Relationship (OIDC)
data "aws_iam_policy_document" "irsa_trust_policy" {
  statement {
    actions = ["sts:AssumeRoleWithWebIdentity"]
    effect  = "Allow"

    principals {
      type        = "Federated"
      identifiers = [local.oidc_provider_arn]
    }

    condition {
      test     = "StringEquals"
      variable = "${local.oidc_provider_url}:sub"
      values   = ["system:serviceaccount:${var.namespace}:${var.service_account_name}"]
    }

    condition {
      test     = "StringEquals"
      variable = "${local.oidc_provider_url}:aud"
      values   = ["sts.amazonaws.com"]
    }
  }
}

# IAM Role para o Service Account
resource "aws_iam_role" "video_notification_worker" {
  name               = "video-notification-worker-role"
  assume_role_policy = data.aws_iam_policy_document.irsa_trust_policy.json

  tags = {
    Name        = "video-notification-worker-role"
    Environment = "production"
    ManagedBy   = "terraform"
  }
}

# IAM Policy Document com permissoes necessarias
data "aws_iam_policy_document" "video_notification_worker_policy" {
  # Permissoes SQS
  statement {
    sid    = "SQSPermissions"
    effect = "Allow"
    actions = [
      "sqs:ReceiveMessage",
      "sqs:DeleteMessage",
      "sqs:GetQueueAttributes",
      "sqs:GetQueueUrl",
      "sqs:ChangeMessageVisibility"
    ]
    resources = [
      "arn:aws:sqs:${var.aws_region}:${local.account_id}:${var.sqs_queue_name}"
    ]
  }

  # Permissoes SNS (opcional, para notificacoes)
  dynamic "statement" {
    for_each = var.sns_topic_arn != "" ? [1] : []
    content {
      sid    = "SNSPermissions"
      effect = "Allow"
      actions = [
        "sns:Publish"
      ]
      resources = [var.sns_topic_arn]
    }
  }

  # Permissoes para logs do CloudWatch
  statement {
    sid    = "CloudWatchLogsPermissions"
    effect = "Allow"
    actions = [
      "logs:CreateLogGroup",
      "logs:CreateLogStream",
      "logs:PutLogEvents"
    ]
    resources = [
      "arn:aws:logs:${var.aws_region}:${local.account_id}:log-group:/aws/eks/${var.cluster_name}/*"
    ]
  }

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue"
        ]
        Resource = "*"
      }
    ]
  })

}

# IAM Policy
resource "aws_iam_policy" "video_notification_worker" {
  name        = "video-notification-worker-policy"
  description = "Policy para o video processing worker acessar SQS, e SNS"
  policy      = data.aws_iam_policy_document.video_notification_worker_policy.json

  tags = {
    Name        = "video-notification-worker-policy"
    Environment = "production"
    ManagedBy   = "terraform"
  }
}

# Attachment da Policy na Role
resource "aws_iam_role_policy_attachment" "video_notification_worker" {
  role       = aws_iam_role.video_notification_worker.name
  policy_arn = aws_iam_policy.video_notification_worker.arn
}

# Kubernetes Service Account com annotation da IAM Role
resource "kubernetes_service_account" "video_notification_worker" {
  metadata {
    name      = var.service_account_name
    namespace = var.namespace
    annotations = {
      "eks.amazonaws.com/role-arn" = aws_iam_role.video_notification_worker.arn
    }
    labels = {
      app       = "video-notification-worker"
      component = "worker"
    }
  }
}