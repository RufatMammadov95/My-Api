package com.example.flights.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GraphiqlController {

	@GetMapping(value = "/graphiql", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String graphiql() {
		return """
				<!DOCTYPE html>
				<html lang="en">
				<head>
					<meta charset="UTF-8">
					<meta name="viewport" content="width=device-width, initial-scale=1.0">
					<title>GraphQL Explorer</title>
					<style>
						* { box-sizing: border-box; }
						body { margin: 0; font-family: Arial, sans-serif; background: #f5f7fb; color: #172033; }
						header { padding: 16px 24px; background: #172033; color: white; }
						main { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; padding: 16px; height: calc(100vh - 64px); }
						.panel { display: flex; flex-direction: column; min-height: 0; }
						.toolbar { display: flex; justify-content: space-between; align-items: center; gap: 12px; margin-bottom: 8px; }
						code { color: #526070; }
						textarea, pre { flex: 1; width: 100%; min-height: 0; margin: 0; padding: 14px; border: 1px solid #ccd3df; border-radius: 6px; background: white; font: 14px/1.45 Consolas, Monaco, monospace; }
						button { border: 0; border-radius: 6px; background: #1f6feb; color: white; padding: 10px 16px; font-weight: 700; cursor: pointer; }
						button:disabled { opacity: .65; cursor: wait; }
						@media (max-width: 800px) { main { grid-template-columns: 1fr; height: auto; } textarea, pre { min-height: 320px; } }
					</style>
				</head>
				<body>
					<header><strong>GraphQL Explorer</strong></header>
					<main>
						<section class="panel">
							<div class="toolbar">
								<span>Query <code id="endpoint"></code></span>
								<button id="run">Run</button>
							</div>
							<textarea id="query">{
				  allFlights {
				    id
				    flightNumber
				    departureCity
				    arrivalCity
				    price
				  }
				}</textarea>
						</section>
						<section class="panel">
							<div class="toolbar"><span>Response</span></div>
							<pre id="result">Ready</pre>
						</section>
					</main>
					<script>
						const params = new URLSearchParams(window.location.search);
						const path = params.get("path") || "/graphql";
						const endpoint = `${location.protocol}//${location.host}${path}`;
						const runButton = document.getElementById("run");
						const queryInput = document.getElementById("query");
						const result = document.getElementById("result");
						document.getElementById("endpoint").textContent = path;

						runButton.addEventListener("click", async () => {
							runButton.disabled = true;
							result.textContent = "Loading...";
							try {
								const response = await fetch(endpoint, {
									method: "POST",
									headers: { "Content-Type": "application/json" },
									body: JSON.stringify({ query: queryInput.value })
								});
								const json = await response.json();
								result.textContent = JSON.stringify(json, null, 2);
							} catch (error) {
								result.textContent = error.message;
							} finally {
								runButton.disabled = false;
							}
						});
					</script>
				</body>
				</html>
				""";
	}
}
