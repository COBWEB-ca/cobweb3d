package cobweb3d.plugins.exchange;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;
import cobwebutil.ArrayUtilities;
import cobwebutil.io.ConfList;
import cobwebutil.io.ConfXMLTag;

public class ExchangeParams extends PerAgentParams<ExchangeAgentParams> {

    private static final long serialVersionUID = 2L;

    @ConfXMLTag("AgentPairs")
    @ConfList(indexName = "AgentPairs", startAtOne = true)
    public ExchangeAgentPairParams[] pairParams;

    public String logPath = "E:\\Data\\Desktop\\t.xlsl";
    private AgentFoodCountable size;

    public ExchangeParams(AgentFoodCountable size) {
        super(ExchangeAgentParams.class, size);
        // nCr + n = unique combos with self-combos too. 0: (1,1)  1: (1,2) 2: (2,2) 3: (1,3) 4:(2,3) 5:(3,3) ...
        pairParams = new ExchangeAgentPairParams[combine(size.getAgentTypes(), 2) + size.getAgentTypes()];//ArrayList<>();
        for (int i = 1; i < size.getAgentTypes() + 1; i++) {
            for (int j = 1; j < size.getAgentTypes() + 1; j++) {
                if (j >= i) {
                    int index = summate(j) + i - 1;
                    if (pairParams[index] == null || pairParams[index].typeOne != i && pairParams[index].typeTwo != j)
                        pairParams[index] = new ExchangeAgentPairParams(i, j);
                }
            }
        }
    }

    /**
     * Find the factorial of a number
     *
     * @param n : number to find the factorial
     * @return : factorial value of 'n'
     */
    public static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

    /**
     * Find the summation from 1 to n
     *
     * @param n : number to find the summation
     * @return : the result of 1 + 2 + ... + n
     */
    public static int summate(int n) {
        int sum = 0;
        for (int i = 1; i < n; i++) sum += i;
        return sum;
    }

    public static int permute(int n, int r) {
        //return factorial(n).divide(factorial(n-r));
        return factorial(n) / factorial(n - r);
    }

    public static int combine(int n, int r) {
        //return factorial(n).divide((factorial(n-r).multiply(factorial(r))));
        return factorial(n) / (factorial(r) * factorial(n - r));
    }

    @Override
    protected ExchangeAgentParams newAgentParam() {
        return new ExchangeAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable newSize) {
        this.size = newSize;
        // TODO: Cull deleted pair params.
        if (pairParams != null) {
            pairParams = ArrayUtilities.resizeArray(pairParams, combine(size.getAgentTypes(), 2) + size.getAgentTypes());
        } else pairParams = new ExchangeAgentPairParams[combine(size.getAgentTypes(), 2) + size.getAgentTypes()];
        for (int i = 1; i < newSize.getAgentTypes() + 1; i++) {
            for (int j = 1; j < newSize.getAgentTypes() + 1; j++) {
                if (j >= i) {
                    int index = summate(j) + i - 1;
                    if (pairParams[index] == null || pairParams[index].typeOne != i && pairParams[index].typeTwo != j)
                        pairParams[index] = new ExchangeAgentPairParams(i, j);
                }
            }
        }
        super.resize(newSize);
    }

    public ExchangeAgentPairParams getPairParams(int typeOne, int typeTwo) {
        int min = typeOne < typeTwo ? typeOne : typeTwo;
        int max = typeOne < typeTwo ? typeTwo : typeOne;
        // start: 1 + 2 + ... + max
        // precision : start + min - 1
        int index = summate(max + 1) + min + 1 - 1;
        if (index >= 0 && index < pairParams.length) return pairParams[index];
        else return null;
    }
}
