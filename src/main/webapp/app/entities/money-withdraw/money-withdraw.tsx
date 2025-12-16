import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './money-withdraw.reducer';

export const MoneyWithdraw = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const moneyWithdrawList = useAppSelector(state => state.moneyWithdraw.entities);
  const loading = useAppSelector(state => state.moneyWithdraw.loading);
  const totalItems = useAppSelector(state => state.moneyWithdraw.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="money-withdraw-heading" data-cy="MoneyWithdrawHeading">
        <Translate contentKey="monsterdamChicksApp.moneyWithdraw.home.title">Money Withdraws</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.moneyWithdraw.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/money-withdraw/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.moneyWithdraw.home.createLabel">Create new Money Withdraw</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {moneyWithdrawList && moneyWithdrawList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('currency')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.currency">Currency</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currency')} />
                </th>
                <th className="hand" onClick={sort('payoutProviderName')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.payoutProviderName">Payout Provider Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('payoutProviderName')} />
                </th>
                <th className="hand" onClick={sort('payoutReferenceId')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.payoutReferenceId">Payout Reference Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('payoutReferenceId')} />
                </th>
                <th className="hand" onClick={sort('processedAt')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.processedAt">Processed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('processedAt')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th className="hand" onClick={sort('withdrawStatus')}>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.withdrawStatus">Withdraw Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('withdrawStatus')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.moneyWithdraw.creator">Creator</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {moneyWithdrawList.map((moneyWithdraw, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/money-withdraw/${moneyWithdraw.id}`} color="link" size="sm">
                      {moneyWithdraw.id}
                    </Button>
                  </td>
                  <td>{moneyWithdraw.amount}</td>
                  <td>{moneyWithdraw.currency}</td>
                  <td>{moneyWithdraw.payoutProviderName}</td>
                  <td>{moneyWithdraw.payoutReferenceId}</td>
                  <td>
                    {moneyWithdraw.processedAt ? (
                      <TextFormat type="date" value={moneyWithdraw.processedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {moneyWithdraw.createdDate ? (
                      <TextFormat type="date" value={moneyWithdraw.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {moneyWithdraw.lastModifiedDate ? (
                      <TextFormat type="date" value={moneyWithdraw.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{moneyWithdraw.createdBy}</td>
                  <td>{moneyWithdraw.lastModifiedBy}</td>
                  <td>
                    {moneyWithdraw.deletedDate ? (
                      <TextFormat type="date" value={moneyWithdraw.deletedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.MoneyWithdrawStatus.${moneyWithdraw.withdrawStatus}`} />
                  </td>
                  <td>
                    {moneyWithdraw.creator ? <Link to={`/user-lite/${moneyWithdraw.creator.id}`}>{moneyWithdraw.creator.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/money-withdraw/${moneyWithdraw.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/money-withdraw/${moneyWithdraw.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/money-withdraw/${moneyWithdraw.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.moneyWithdraw.home.notFound">No Money Withdraws found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={moneyWithdrawList && moneyWithdrawList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default MoneyWithdraw;
