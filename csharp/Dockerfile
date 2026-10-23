FROM mcr.microsoft.com/dotnet/sdk:8.0.424-jammy

LABEL maintainer="Avature Engineering" \
      version="1.0.0"

ENV DOTNET_CLI_TELEMETRY_OPTOUT=1 \
    DOTNET_NOLOGO=1 \
    DOTNET_USE_POLLING_FILE_WATCHER=true \
    JOB_NOTIFIER_BUILD_ROOT=/tmp/job-notifier-build/ \
    NUGET_PACKAGES=/home/app/.nuget/packages

WORKDIR /app

COPY --chown=app:app JobNotifier.sln Directory.Build.props ./
COPY --chown=app:app src/JobNotifier/JobNotifier.csproj src/JobNotifier/
COPY --chown=app:app tests/JobNotifier.Tests/JobNotifier.Tests.csproj tests/JobNotifier.Tests/
RUN dotnet restore JobNotifier.sln

COPY --chown=app:app . .
RUN dotnet build JobNotifier.sln --no-restore \
    && mkdir -p /app/data /home/app/.nuget/packages /tmp/job-notifier-build \
    && chown -R app:app /app /home/app/.nuget /tmp/job-notifier-build \
    && find / -xdev -type f -perm /6000 -exec chmod a-s {} +

USER app

EXPOSE 7000

HEALTHCHECK --interval=5s --timeout=3s --start-period=30s --retries=30 \
    CMD curl --fail http://localhost:7000/ || exit 1

ENTRYPOINT ["/app/docker-entrypoint.sh"]
CMD ["dotnet", "watch", "--project", "src/JobNotifier/JobNotifier.csproj", "run", "--no-restore", "--no-hot-reload"]
