{{/*
Expand the name of the chart.
*/}}
{{- define "mona-matti.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "mona-matti.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}

{{/*
Chart name and version.
*/}}
{{- define "mona-matti.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" }}
{{- end }}

{{/*
Common Labels
*/}}
{{- define "mona-matti.labels" -}}
helm.sh/chart: {{ include "mona-matti.chart" . }}
app.kubernetes.io/name: {{ include "mona-matti.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector Labels
*/}}
{{- define "mona-matti.selectorLabels" -}}
app.kubernetes.io/name: {{ include "mona-matti.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}s