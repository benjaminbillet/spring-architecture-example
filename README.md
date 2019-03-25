# Finishing web configurations
- Access control: see `my.app.config.SecurityConfiguration#configure`.
- Enabled [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS): see `my.app.config.ServletConfiguration#corsFilter`.
- HTTP caching headers for static assets: see `my.app.config.ServletConfiguration#onStartup`.
