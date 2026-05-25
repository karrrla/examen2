package com.escom.examenfirebase.utils

object Constants {
    const val USERS_COLLECTION = "users"
    const val NOTIFICATIONS_COLLECTION = "notifications"

    const val ROLE_USER = "user"
    const val ROLE_ADMIN = "admin"

    // IMPORTANTE: Reemplaza con tu Project ID de Firebase (de google-services.json: "project_id")
    const val FIREBASE_PROJECT_ID = "examenfirebaseapp"

    // IMPORTANTE: Pega aquí el contenido del JSON de la service account
    // descargado desde: Firebase Console > Configuración del proyecto > Cuentas de servicio
    // > Generar nueva clave privada

    const val SERVICE_ACCOUNT_JSON ="""
        {
            "type": "service_account",
            "project_id": "examenfirebaseapp",
            "private_key_id": "ed654c1fd0986328fd8123b352f42e602583f900",
            "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC6FXKeynywQDMe\nljA/Ljmj9hgqIjRSJGId5sZW53dV7l5bQKYQDyQBaP1i09P/pSCpRUcGudgTuyLO\nSd9KaauUjIOiO87u4tVndNBFYLo9+5aLKWPOOhda9c8Goe0C6GAfjy36z9NqfrQC\nYvt7juy+WlvGt7hm1zj8WkJJwHv1dZe+WjZjqHtj0zU5R20pLBM+6F4uys0qzBQ8\ngZJ2o1zUnn7gvbakILNQcIQBe49V+OGJyuKzLOiWMB+7aislwojy5TDbFE32QG4s\n1DIdP9iybI87Snnvdqu/NhxqpLrNo3Qi4Zgq1mB9jBZsZvWhHzaZtGkLCjQ68B/V\n61dkNFBTAgMBAAECggEAVrlrN304Wm45RN6LMs7o1zT48x4I02WYgsYMlvD+aYKT\n4CSDoIVl6vBS8021Bnaff7RFU2mmvh9fq9O66LPR3wQTzzbfjO8TtUk4TbU0Z3HL\nKiVKmhDx1Jct+rHZw5qoPd0deUCFkCLn4xrdiKrGRDrZfgYev3M74ERyk2vNecoe\nUOGNmo8zDMcRWnB7Zgq22Brqs8/+PLo3Rwt0D5lVuTl/s1qjSHKeIHmaQ4eyS+lv\nyqEcz40Ji7BVq/Ke24u3WLmmBiol8ad0P6Bk8gm1pVp0lZm8lTYPNZNcG+//WU3Z\nOUoeCFePLY+8X9nFbKOQoLjoft1qQ8wCWvJOmvdGYQKBgQDcAhXlct83dogRzFMR\nOh9jHaFqHJ1w+ayEKqXYqVYKuUZ7ZvjeuZj3X0pWeRjS1PJlU0qANNYC6m/Yfk+a\nppxEYeq/4uSQhrd/7KDCJfYNcheOX1Uyca0O/k3LFCHzOtbI240cO8KPoJwSu7SU\nUCbH+JTaI8O/zgJcSpsmk0jKcwKBgQDYhp0lwyvtV5c7mhk+Q1umpdljVEN6fCRn\nLbD4jtPJVCwxDAtEih1Vr+J+zpOpGZZhjyhSKSsD2t1L0RoFckZfwI5MCs302Oyx\nMOMCDCk69qQiLenwBLUWMz9HP5HgXhdBdP1QXgSpdv+YnJ4i9F8w715M2uE2tOX3\nwLs2psmKoQKBgHeR4pibP9mC4BzcMWExd5GErw9ekequpyYFj7EbBxo466dxpVxW\n/l2jOmGY2vbqqPqMs/bNwGpBQ5wjudvRJSi9FrpG8/XgYaZy0kPyIbHZChhkk4cg\n5O87Ish2HqV0mvr658wqZmls3qYHwfLBRPJ1lWChA2gg7IOAQKZMccklAoGAKIqz\n+/EaVXtUDs3gsh7Ml1xwYr/RTqrV6hfNDg04LHc9OwXFSlUN/xBKuXGWhlALL0Mt\nyox4yGT/kIWbeK+X66/TNPvE6nZ/gwHjT2vVgHvT6a5tIgrPmVOJIp8NglQ7yH7O\nO3EPMGfencWdenAFLtVy87L7/lFKyePlaes6y2ECgYBsNIkhVXPljZfS7yGMGj7C\nhoMuJf4MB1BwxSUjHZllh01Wd7veyQqZmiOHHNk3YFUs92azayiLqmKp6NUlLo4J\n5nsc6NatAkWJC6wufwy/xvHkX8LAmpLcxgiWyJslSTmPLZJNnin4xRHhOm2g5FGU\n//THR0Kzg9heo4dB8ERpdA==\n-----END PRIVATE KEY-----\n",
            "client_email": "firebase-adminsdk-fbsvc@examenfirebaseapp.iam.gserviceaccount.com",
            "client_id": "115257647332883715282",
            "auth_uri": "https://accounts.google.com/o/oauth2/auth",
            "token_uri": "https://oauth2.googleapis.com/token",
            "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
            "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40examenfirebaseapp.iam.gserviceaccount.com",
            "universe_domain": "googleapis.com"
        }
        """
}
