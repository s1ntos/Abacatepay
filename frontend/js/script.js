function comprar(produto, valor) {
  const cliente = {
    name: "Paulo Pacheco",
    cellphone: "(79) 98838-8821",
    email: "paulo.pacheco@example.com",
    taxId: "264.934.510-19"
  };

  const valorNumerico = parseFloat(valor); // garante que é número, não string

  console.log("Enviando:", { produto, valor: valorNumerico, cliente });

  fetch('http://localhost:8080/api/pagar', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ produto, valor: valorNumerico, cliente })
  })
  .then(res => res.json())
  .then(dados => {
    console.log("Checkout criado:", dados);
    const urlCheckout = dados?.data?.url;
    if (urlCheckout) {
      window.location.href = urlCheckout;
    } else {
      alert("Erro ao obter URL do pagamento.");
    }
  })
  .catch(err => {
    console.error("Erro:", err);
    alert("Erro ao conectar com a API.");
  });
}
